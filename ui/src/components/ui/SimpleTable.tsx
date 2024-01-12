import { Table, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";
import { flexRender, Table as ReactTable } from "@tanstack/react-table";
import Pagination from "./Pagination";

interface SimpleTableProps<T extends object> {
  table: ReactTable<T>;
}

function SimpleTable<T extends object>({ table }: SimpleTableProps<T>) {
  return (
    <>
      <Table className="flex-grow">
        <Thead className="leading-10">
          {table.getHeaderGroups().map((headerGroup) => (
            <Tr key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <Th key={header.id}>
                  {header.isPlaceholder
                    ? null
                    : flexRender(
                        header.column.columnDef.header,
                        header.getContext(),
                      )}
                </Th>
              ))}
            </Tr>
          ))}
        </Thead>
        <Tbody>
          {table.getRowModel().rows.map((row) => (
            <Tr key={row.id}>
              {row.getVisibleCells().map((cell) => (
                <Td key={cell.id}>
                  {flexRender(cell.column.columnDef.cell, cell.getContext())}
                </Td>
              ))}
            </Tr>
          ))}
        </Tbody>
      </Table>
      <Pagination table={table} />
    </>
  );
}

export default SimpleTable;
