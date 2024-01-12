import { Box, HTMLChakraProps } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useGetNetworkHistory } from "services/MiscService";
import { echarts, ECLineOption } from "configs";
import numeral from "numeral";

interface NetworkGrowthChartProps extends HTMLChakraProps<"div"> {
  months?: number;
}

const NetworkGrowthChart = ({
  months = 12,
  ...props
}: NetworkGrowthChartProps) => {
  const chartRef = React.useRef<HTMLDivElement>(null);
  const getNetworkHistory = useGetNetworkHistory({
    variables: { months },
  });
  useEffect(() => {
    if (chartRef.current === null || getNetworkHistory.data === undefined) {
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
        data: ["Read", "Write"],
      },
      yAxis: {
        type: "category",
        data: getNetworkHistory.data.read.map((item) => item.date).reverse(),
      },
      xAxis: {
        type: "value",
        name: "Bytes",
        axisLabel: {
          formatter: (value: number) => {
            return numeral(value).format("0 b");
          },
        },
      },
      grid: {
        left: 60,
        bottom: 45,
        right: 60,
      },
      series: [
        {
          name: "Read",
          type: "bar",
          data: getNetworkHistory.data.read.map((item) => item.value).reverse(),
          tooltip: {
            valueFormatter: (value) => {
              return numeral(value).format("0[.]00 b");
            },
          },
        },
        {
          name: "Write",
          type: "bar",
          data: getNetworkHistory.data.write
            .map((item) => item.value)
            .reverse(),
          tooltip: {
            valueFormatter: (value) => {
              return numeral(value).format("0[.]00 b");
            },
          },
        },
      ],
    };
    chart.setOption(options);
  }, [chartRef, getNetworkHistory.data]);
  return <Box ref={chartRef} {...props}></Box>;
};

export default NetworkGrowthChart;
