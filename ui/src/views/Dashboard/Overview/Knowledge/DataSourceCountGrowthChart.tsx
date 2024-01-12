import { Box, HTMLChakraProps } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useGetDataSourceHistory } from "services/MiscService";
import { echarts, ECLineOption } from "configs";
import numeral from "numeral";

interface DataSourceCountGrowthChartProps extends HTMLChakraProps<"div"> {
  months?: number;
}

const DataSourceCountGrowthChart = ({
  months = 20,
  ...props
}: DataSourceCountGrowthChartProps) => {
  const chartRef = React.useRef<HTMLDivElement>(null);
  const getDataSourceHistory = useGetDataSourceHistory({
    variables: { months },
  });
  useEffect(() => {
    if (chartRef.current === null || getDataSourceHistory.data === undefined) {
      return;
    }
    const chart =
      echarts.getInstanceByDom(chartRef.current) ||
      echarts.init(chartRef.current);
    const options: ECLineOption = {
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow",
        },
      },
      legend: {
        top: 20,
        data: ["File", "Website", "Database"],
      },
      xAxis: {
        type: "category",
        data: getDataSourceHistory.data.file.map((item) => item.date),
      },
      yAxis: {
        type: "value",
        axisLabel: {
          formatter: (value: number) => {
            return numeral(value).format("0 a");
          },
        },
      },
      grid: {
        left: 60,
        bottom: 45,
        right: 20,
      },
      series: [
        {
          name: "File",
          type: "bar",
          stack: "Knowledge",
          data: getDataSourceHistory.data.file.map((item) => item.value),
        },
        {
          name: "Website",
          type: "bar",
          stack: "Knowledge",
          data: getDataSourceHistory.data.website.map((item) => item.value),
        },
        {
          name: "Database",
          type: "bar",
          stack: "Knowledge",
          data: getDataSourceHistory.data.database.map((item) => item.value),
        },
      ],
    };
    chart.setOption(options);
  }, [chartRef, getDataSourceHistory.data]);
  return <Box ref={chartRef} {...props}></Box>;
};

export default DataSourceCountGrowthChart;
