import { RowData, Table } from "@tanstack/react-table";
import {
  Button,
  HStack,
  Icon,
  IconButton,
  Spacer,
  useBoolean,
} from "@chakra-ui/react";
import Select from "react-select";
import {
  HiOutlineChevronDoubleLeft,
  HiOutlineChevronDoubleRight,
  HiOutlineChevronLeft,
  HiOutlineChevronRight,
} from "react-icons/hi";
import { useMemo } from "react";
import { HiOutlineEllipsisHorizontal } from "react-icons/hi2";
import range from "lodash-es/range";

const MIN_ELLIPSE_COUNT = 3;

interface PaginationProps<TData extends RowData> {
  table: Table<TData>;
}

function getPagerArray(
  showPrevMore: boolean,
  showNextMore: boolean,
  pageIndex: number,
  pageCount: number,
) {
  const leftBound = showPrevMore
    ? Math.min(pageIndex + Math.floor(MIN_ELLIPSE_COUNT / 2), pageCount - 2) -
      MIN_ELLIPSE_COUNT
    : 1;
  const rightBound = showNextMore
    ? Math.max(pageIndex - Math.floor(MIN_ELLIPSE_COUNT / 2), 1) +
      MIN_ELLIPSE_COUNT
    : pageCount - 1;
  return range(leftBound, rightBound);
}

function Pagination<TData extends RowData>({ table }: Readonly<PaginationProps<TData>>) {
  const [isHoverShowPrevMore, setIsHoverShowPrevMore] = useBoolean(false);
  const [isHoverShowNextMore, setIsHoverShowNextMore] = useBoolean(false);
  const { pageIndex, pageSize } = table.getState().pagination;
  const pageCount = table.getPageCount();

  const { showPrevMore, showNextMore, pagerArray } = useMemo(() => {
    let showPrevMore = false;
    let showNextMore = false;
    const leadingDistance = pageIndex - 1;
    const trailingDistance = pageCount - pageIndex - 1;
    if (leadingDistance > MIN_ELLIPSE_COUNT) {
      showPrevMore = true;
    }
    if (trailingDistance > MIN_ELLIPSE_COUNT) {
      showNextMore = true;
    }
    setIsHoverShowNextMore.off();
    setIsHoverShowPrevMore.off();
    const pagerArray = getPagerArray(
      showPrevMore,
      showNextMore,
      pageIndex,
      pageCount,
    );
    return { showPrevMore, showNextMore, pagerArray };
  }, [pageIndex, pageSize, pageCount]);

  return (
    <HStack gap={2} justifyContent={"center"} py={5}>
      <IconButton
        variant={"paginationButton"}
        icon={<Icon as={HiOutlineChevronLeft} />}
        onClick={() => table.previousPage()}
        disabled={!table.getCanPreviousPage()}
        aria-label={"previous page"}
      />
      <Button
        variant={"paginationButton"}
        size={"sm"}
        onClick={() => table.setPageIndex(0)}
        bg={pageIndex === 0 ? "purple.200" : undefined}
        disabled={pageIndex <= 0}
      >
        1
      </Button>
      {showPrevMore && (
        <IconButton
          icon={
            <Icon
              as={
                isHoverShowPrevMore
                  ? HiOutlineChevronDoubleLeft
                  : HiOutlineEllipsisHorizontal
              }
            />
          }
          variant={"paginationButton"}
          fontSize={"sm"}
          onClick={() => table.setPageIndex(pageIndex - MIN_ELLIPSE_COUNT)}
          onMouseOver={setIsHoverShowPrevMore.on}
          onMouseOut={setIsHoverShowPrevMore.off}
          aria-label={"show previous more"}
        />
      )}
      {pagerArray.map((page) => (
        <Button
          variant={"paginationButton"}
          size={"sm"}
          key={page}
          onClick={() => table.setPageIndex(page)}
          bg={pageIndex === page ? "purple.200" : undefined}
        >
          {page + 1}
        </Button>
      ))}
      {showNextMore && (
        <IconButton
          variant={"paginationButton"}
          icon={
            <Icon
              as={
                isHoverShowNextMore
                  ? HiOutlineChevronDoubleRight
                  : HiOutlineEllipsisHorizontal
              }
            />
          }
          fontSize={"sm"}
          onClick={() => table.setPageIndex(pageIndex + MIN_ELLIPSE_COUNT)}
          onMouseOver={setIsHoverShowNextMore.on}
          onMouseOut={setIsHoverShowNextMore.off}
          aria-label={"show next more"}
        />
      )}
      {pageCount > 1 && (
        <Button
          variant={"paginationButton"}
          size={"sm"}
          onClick={() => table.setPageIndex(pageCount - 1)}
          bg={pageIndex === pageCount - 1 ? "purple.200" : undefined}
          disabled={pageIndex >= pageCount - 1}
        >
          {pageCount}
        </Button>
      )}
      <IconButton
        variant={"paginationButton"}
        icon={<Icon as={HiOutlineChevronRight} />}
        onClick={() => table.nextPage()}
        disabled={pageIndex >= pageCount - 1}
        aria-label={"next page"}
      />
      <Spacer />
      <Select
        options={[10, 20, 30, 40, 50].map((pageSize) => ({
          value: pageSize,
          label: `${pageSize} / Page`,
        }))}
        defaultValue={{ value: pageSize, label: `${pageSize} / Page` }}
        menuPlacement={"auto"}
        onChange={(newValue) => table.setPageSize(newValue?.value ?? 10)}
        components={{
          IndicatorSeparator: null,
        }}
      />
    </HStack>
  );
}

export default Pagination;
