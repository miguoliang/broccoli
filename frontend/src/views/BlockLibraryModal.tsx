/**
 * True leadership only exists if people follow when they have the freedom to not follow.
 * Otherwise, it's just power. And power is not leadership. -- Jim Collins
 */
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  GridItem,
  Heading,
  Input,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalHeader,
  ModalOverlay,
  SimpleGrid,
  VStack,
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";

interface BlockLibraryModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const library = [
  {
    name: "data source",
    blocks: [
      {
        name: "file",
        description: "read data from file",
      },
      {
        name: "web",
        description: "read data from web",
      },
      {
        name: "image",
        description: "read data from image",
      },
      {
        name: "database",
        description: "read data from database",
      },
    ],
  },
];

const BlockLibraryModal = ({
  isOpen,
  onClose,
}: Readonly<BlockLibraryModalProps>) => {
  const { t } = useTranslation();

  return (
    <Modal isOpen={isOpen} onClose={onClose} size={"4xl"}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>{t("block library")}</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <LibraryFilter />
        </ModalBody>
      </ModalContent>
    </Modal>
  );
};

const LibraryFilter = () => {
  const { t } = useTranslation();

  return (
    <SimpleGrid columns={4} gap={4}>
      <GridItem as={VStack} alignItems={"stretch"}>
        <Input variant={"filled"} placeholder={t("search")} />
        {library.map(({ name }) => (
          <Button key={name}>{t(name)}</Button>
        ))}
      </GridItem>
      <GridItem as={VStack} colSpan={3} alignItems={"stretch"}>
        {library.map((category) => (
          <LibraryCategory key={category.name} {...category} />
        ))}
      </GridItem>
    </SimpleGrid>
  );
};

const LibraryCategory = ({ name, blocks }: Readonly<(typeof library)[0]>) => {
  const { t } = useTranslation();
  return (
    <>
      <Heading size={"sm"}>{t(name)}</Heading>
      <SimpleGrid columns={3} gap={4}>
        {blocks.map(({ name, description }) => (
          <Card key={name} as={GridItem}>
            <CardHeader>
              <Heading>{t(name)}</Heading>
            </CardHeader>
            <CardBody>{t(description)}</CardBody>
          </Card>
        ))}
      </SimpleGrid>
    </>
  );
};

export default BlockLibraryModal;
