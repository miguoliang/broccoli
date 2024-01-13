import { Box, HTMLChakraProps } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useGetComputeHistory } from "services/MiscService";
import { echarts, ECLineOption } from "configs";
import numeral from "numeral";

interface ComputeGrowthChartProps extends HTMLChakraProps<"div"> {
  months?: number;
}

const ComputeGrowthChart = ({
  months = 12,
  ...props
}: ComputeGrowthChartProps) => {
  const chartRef = React.useRef<HTMLDivElement>(null);
  const getComputeHistory = useGetComputeHistory({
    variables: { months },
  });
  useEffect(() => {
    if (chartRef.current === null || getComputeHistory.data === undefined) {
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
        show: false,
      },
      xAxis: {
        type: "category",
        data: getComputeHistory.data.hours.map((item) => item.date).reverse(),
      },
      yAxis: {
        type: "value",
        name: "Hours",
        axisLabel: {
          formatter: (value: number) => {
            return numeral(value).format("0,0[.]00a");
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
          name: "Hours",
          type: "line",
          data: getComputeHistory.data.hours
            .map((item) => item.value)
            .reverse(),
          tooltip: {
            valueFormatter: (value) => {
              return numeral(value).format("0,0[.]00a");
            },
          },
        },
      ],
    };
    chart.setOption(options);
  }, [chartRef, getComputeHistory.data]);
  return <Box ref={chartRef} {...props}></Box>;
};

export default ComputeGrowthChart;
