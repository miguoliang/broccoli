import { Box, HTMLChakraProps } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useGetStorageHistory } from "../../../../services/MiscService";
import { echarts, ECLineOption } from "../../../../configs";
import numeral from "numeral";

interface StorageGrowthChartProps extends HTMLChakraProps<"div"> {
  months?: number;
}

const StorageGrowthChart = ({
  months = 12,
  ...props
}: StorageGrowthChartProps) => {
  const chartRef = React.useRef<HTMLDivElement>(null);
  const getStorageHistory = useGetStorageHistory({
    variables: { months },
  });
  useEffect(() => {
    if (chartRef.current === null || getStorageHistory.data === undefined) {
      return;
    }
    const chart =
      echarts.getInstanceByDom(chartRef.current) ??
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
        data: ["Database", "File", "Usage"],
      },
      xAxis: {
        type: "category",
        data: getStorageHistory.data.database.map((item) => item.date),
      },
      yAxis: [
        {
          type: "value",
          name: "Size",
          axisLabel: {
            formatter: (value: number) => {
              return numeral(value).format("0 b");
            },
          },
        },
        {
          type: "value",
          name: "Percentage",
          max: 1,
          min: 0,
          axisLabel: {
            formatter: "{value} %",
          },
        },
      ],
      grid: {
        left: 60,
        bottom: 45,
        right: 60,
      },
      series: [
        {
          name: "Database",
          type: "bar",
          data: getStorageHistory.data.database.map((item) => item.value),
          tooltip: {
            valueFormatter: (value) => {
              return numeral(value).format("0[.]00 b");
            },
          },
        },
        {
          name: "File",
          type: "bar",
          data: getStorageHistory.data.file.map((item) => item.value),
          tooltip: {
            valueFormatter: (value) => {
              return numeral(value).format("0[.]00 b");
            },
          },
        },
        {
          name: "Usage",
          type: "line",
          yAxisIndex: 1,
          data: getStorageHistory.data.usage.map((item) => item.value),
          tooltip: {
            valueFormatter: (value) => {
              return numeral(value).format("0[.]00%");
            },
          },
        },
      ],
    };
    chart.setOption(options);
  }, [chartRef, getStorageHistory.data]);
  return <Box ref={chartRef} {...props}></Box>;
};

export default StorageGrowthChart;
