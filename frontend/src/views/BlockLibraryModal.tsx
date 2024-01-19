/**
 * True leadership only exists if people follow when they have the freedom to not follow.
 * Otherwise, it's just power. And power is not leadership. -- Jim Collins
 */
import {
  Card,
  CardBody,
  CardHeader,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalHeader,
  ModalOverlay,
  SimpleGrid,
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import useStore from "../hooks/useReactFlowState";

interface BlockLibraryModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const library = [
  {
    name: "product",
    description: "read data from file",
  },
  {
    name: "manufacturer",
    description: "read data from web",
  },
  {
    name: "application",
    description: "read data from image",
  },
  {
    name: "origin",
    description: "read data from database",
  },
  {
    name: "market",
    description: "read data from database",
  },
];

const BlockLibraryModal = ({
  isOpen,
  onClose,
}: Readonly<BlockLibraryModalProps>) => {
  const { t } = useTranslation();
  const { nodes, setNodes } = useStore();

  const onNodePicked = (name: string) => {
    setNodes([
      ...nodes,
      {
        id: Date.now().toString(),
        type: name,
        position: { x: 250, y: 250 },
        data: {},
      },
    ]);
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"4xl"}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>{t("block library")}</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <LibraryFilter onNodePicked={onNodePicked} />
        </ModalBody>
      </ModalContent>
    </Modal>
  );
};

interface LibraryFilterProps {
  onNodePicked: (name: string) => void;
}

const LibraryFilter = ({ onNodePicked }: LibraryFilterProps) => {
  const { t } = useTranslation();

  return (
    <SimpleGrid columns={4} spacing={4}>
      {library.map(({ name, description }) => (
        <Card
          key={name}
          onClick={() => onNodePicked(name)}
          size={"sm"}
          cursor={"pointer"}
          border={"1px"}
          borderColor={"transparent"}
          transition="transform 0.1s ease-in-out"
          _hover={{ transform: "scale(1.02)", borderColor: "gray.500" }}
        >
          <CardHeader>{t(name)}</CardHeader>
          <CardBody>{t(description)}</CardBody>
        </Card>
      ))}
    </SimpleGrid>
  );
};

export default BlockLibraryModal;
