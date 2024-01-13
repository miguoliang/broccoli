import { DataSource } from "models";
import {
  createColumnHelper,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { Link } from "react-router-dom";
import { usePagination } from "hooks";
import { SimpleTable } from "components/ui";
import { Button, Icon, Text } from "@chakra-ui/react";
import { PageHeader } from "../components";
import { useGetDataSources } from "services";
import dayjs from "dayjs";
import { HiOutlineChevronDoubleRight } from "react-icons/hi";

const columnHelper = createColumnHelper<DataSource>();
const columns = [
  columnHelper.display({
    header: "Row number",
    cell: ({ row, table }) => {
      const { pageIndex, pageSize } = table.getState().pagination;
      return <Text>{pageIndex * pageSize + row.index + 1}</Text>;
    },
  }),
  columnHelper.accessor("title", { header: "Title" }),
  columnHelper.accessor("createdAt", {
    header: "Created at",
    cell: ({ row }) =>
      dayjs(row.original.createdAt).format("YYYY-MM-DD HH:mm:ss"),
  }),
  columnHelper.display({
    header: "Operations",
    cell: ({ row }) => (
      <Button
        size={"sm"}
        rightIcon={<Icon as={HiOutlineChevronDoubleRight} />}
        as={Link}
        to={{
          pathname: `/dashboard/graph`,
          search: "?dataSourceId=" + row.original.id,
        }}
      >
        Open Graph
      </Button>
    ),
  }),
];

const DataSourceComponent = () => {
  const [pagination, setPagination] = usePagination();

  const { data } = useGetDataSources({
    variables: {
      date: dayjs().startOf("day").toDate(),
      page: pagination.pageIndex,
      size: pagination.pageSize,
    },
    keepPreviousData: true,
  });

  const table = useReactTable({
    data: data?.content ?? [],
    columns,
    pageCount: data?.totalPages ?? 1,
    onPaginationChange: setPagination,
    state: {
      pagination,
    },
    manualPagination: true,
    getCoreRowModel: getCoreRowModel<DataSource>(),
    debugTable: true,
  });

  return (
    <>
      <PageHeader title={"Data Sources"} />
      <SimpleTable table={table} />
    </>
  );
};

export default DataSourceComponent;
