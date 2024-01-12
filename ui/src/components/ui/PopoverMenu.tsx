import {
  autoUpdate,
  flip,
  FloatingFocusManager,
  FloatingList,
  FloatingNode,
  FloatingPortal,
  FloatingTree,
  offset,
  safePolygon,
  shift,
  useDismiss,
  useFloating,
  useFloatingNodeId,
  useFloatingParentNodeId,
  useFloatingTree,
  useHover,
  useInteractions,
  useListItem,
  useListNavigation,
  useMergeRefs,
  useRole,
  useTypeahead,
} from "@floating-ui/react";
import * as React from "react";
import noop from "lodash-es/noop";
import { Box, Button, HStack, Icon, IconButton, Text } from "@chakra-ui/react";
import { NavigationMenuItem } from "configs";
import { HiChevronRight } from "react-icons/hi";
import { useSideNav } from "hooks";
import { useNavigate } from "react-router-dom";

const MenuContext = React.createContext<{
  getItemProps: (
    userProps?: React.HTMLProps<HTMLElement>,
  ) => Record<string, unknown>;
  activeIndex: number | null;
  setActiveIndex: React.Dispatch<React.SetStateAction<number | null>>;
  setHasFocusInside: React.Dispatch<React.SetStateAction<boolean>>;
  isOpen: boolean;
}>({
  getItemProps: () => ({}),
  activeIndex: null,
  setActiveIndex: noop,
  setHasFocusInside: noop,
  isOpen: false,
});

const MenuComponent = React.forwardRef<
  HTMLButtonElement,
  {
    item: NavigationMenuItem;
  } & React.HTMLProps<HTMLButtonElement>
>(({ item: menuItem, children, ...props }, forwardedRef) => {
  const sideNav = useSideNav();
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = React.useState(false);
  const [hasFocusInside, setHasFocusInside] = React.useState(false);
  const [activeIndex, setActiveIndex] = React.useState<number | null>(null);

  const elementsRef = React.useRef<Array<HTMLButtonElement | null>>([]);
  const labelsRef = React.useRef<Array<string | null>>([]);
  const parent = React.useContext(MenuContext);

  const tree = useFloatingTree();
  const nodeId = useFloatingNodeId();
  const parentId = useFloatingParentNodeId();
  const item = useListItem();

  const isNested = parentId != null;

  const { floatingStyles, refs, context } = useFloating<HTMLButtonElement>({
    nodeId,
    open: isOpen,
    onOpenChange: setIsOpen,
    placement: "right-start",
    middleware: [
      offset({ mainAxis: isNested ? 20 : 10, crossAxis: isNested ? -9 : 0 }),
      flip(),
      shift(),
    ],
    whileElementsMounted: autoUpdate,
  });

  const hover = useHover(context, {
    enabled: true,
    delay: { open: 75 },
    handleClose: safePolygon({ blockPointerEvents: true }),
  });
  const role = useRole(context, { role: "menu" });
  const dismiss = useDismiss(context, { bubbles: true });
  const listNavigation = useListNavigation(context, {
    listRef: elementsRef,
    activeIndex,
    nested: isNested,
    onNavigate: setActiveIndex,
  });
  const typeahead = useTypeahead(context, {
    listRef: labelsRef,
    onMatch: isOpen ? setActiveIndex : undefined,
    activeIndex,
  });

  const { getReferenceProps, getFloatingProps, getItemProps } = useInteractions(
    [hover, role, dismiss, listNavigation, typeahead],
  );

  // Event emitter allows you to communicate across tree components.
  // This effect closes all menus when an item gets clicked anywhere
  // in the tree.
  React.useEffect(() => {
    if (!tree) return;

    function handleTreeClick() {
      setIsOpen(false);
    }

    function onSubMenuOpen(event: { nodeId: string; parentId: string }) {
      if (event.nodeId !== nodeId && event.parentId === parentId) {
        setIsOpen(false);
      }
    }

    tree.events.on("click", handleTreeClick);
    tree.events.on("menuopen", onSubMenuOpen);

    return () => {
      tree.events.off("click", handleTreeClick);
      tree.events.off("menuopen", onSubMenuOpen);
    };
  }, [tree, nodeId, parentId]);

  React.useEffect(() => {
    if (isOpen && tree) {
      tree.events.emit("menuopen", { parentId, nodeId });
    }
  }, [tree, isOpen, nodeId, parentId]);

  const commonProps = {
    ref: useMergeRefs([refs.setReference, item.ref, forwardedRef]),
    tabIndex: !isNested
      ? undefined
      : parent.activeIndex === item.index
      ? 0
      : -1,
    variant: isNested ? "dropdownMenuItem" : "unstyled",
    role: isNested ? "menuitem" : undefined,
    "data-open": isOpen ? "" : undefined,
    "data-nested": isNested ? "" : undefined,
    "data-focus-inside": hasFocusInside ? "" : undefined,
    ...getReferenceProps(
      parent.getItemProps({
        ...props,
        onFocus(event: React.FocusEvent<HTMLButtonElement>) {
          props.onFocus?.(event);
          setHasFocusInside(false);
          parent.setHasFocusInside(true);
        },
      }),
    ),
  };

  const properButton =
    sideNav.collapsed && !isNested ? (
      <IconButton
        aria-label={menuItem.title}
        fontSize={"2xl"}
        icon={menuItem.icon}
        bg={"transparent"}
        onClick={() =>
          menuItem.type === "item" && menuItem.path && navigate(menuItem.path)
        }
        {...commonProps}
      />
    ) : (
      <Button
        {...commonProps}
        onClick={() =>
          menuItem.type === "item" && menuItem.path && navigate(menuItem.path)
        }
      >
        <HStack w={"full"}>
          {menuItem.icon}
          {isNested && <Text flexGrow={1}>{menuItem.title}</Text>}
          {isNested && <Icon as={HiChevronRight} />}
        </HStack>
      </Button>
    );

  return (
    <FloatingNode id={nodeId}>
      {properButton}
      <MenuContext.Provider
        value={{
          activeIndex,
          setActiveIndex,
          getItemProps,
          setHasFocusInside,
          isOpen,
        }}
      >
        <FloatingList elementsRef={elementsRef} labelsRef={labelsRef}>
          {isOpen && (
            <FloatingPortal>
              <FloatingFocusManager
                context={context}
                modal={false}
                initialFocus={isNested ? -1 : 0}
                returnFocus={!isNested}
              >
                <Box
                  ref={refs.setFloating}
                  style={floatingStyles}
                  {...getFloatingProps()}
                  bg={"white"}
                  borderRadius={"lg"}
                  borderWidth={1}
                  p={2}
                >
                  {children}
                </Box>
              </FloatingFocusManager>
            </FloatingPortal>
          )}
        </FloatingList>
      </MenuContext.Provider>
    </FloatingNode>
  );
});

const MenuItem = React.forwardRef<
  HTMLButtonElement,
  {
    item: NavigationMenuItem;
    disabled?: boolean;
  } & React.ButtonHTMLAttributes<HTMLButtonElement>
>(({ item: menuItem, disabled, ...props }, forwardedRef) => {
  const menu = React.useContext(MenuContext);
  const item = useListItem({ label: disabled ? null : menuItem.title });
  const tree = useFloatingTree();
  const isActive = item.index === menu.activeIndex;
  const navigate = useNavigate();

  return (
    <Button
      {...props}
      ref={useMergeRefs([item.ref, forwardedRef])}
      type="button"
      fontSize={"md"}
      role="menuitem"
      variant={"dropdownMenuItem"}
      tabIndex={isActive ? 0 : -1}
      disabled={disabled}
      {...menu.getItemProps({
        onClick(event: React.MouseEvent<HTMLButtonElement>) {
          props.onClick?.(event);
          tree?.events.emit("click");
          menuItem.type === "item" && menuItem.path && navigate(menuItem.path);
        },
        onFocus(event: React.FocusEvent<HTMLButtonElement>) {
          props.onFocus?.(event);
          menu.setHasFocusInside(true);
        },
      })}
    >
      {menuItem.title}
    </Button>
  );
});

const Menu = React.forwardRef<
  HTMLButtonElement,
  {
    item: NavigationMenuItem;
  } & React.HTMLProps<HTMLButtonElement>
>((props, ref) => {
  const parentId = useFloatingParentNodeId();

  if (parentId === null) {
    return (
      <FloatingTree>
        <MenuComponent {...props} ref={ref} />
      </FloatingTree>
    );
  }

  return <MenuComponent {...props} ref={ref} />;
});

export { Menu, MenuItem };
