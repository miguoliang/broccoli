import { Box, HTMLChakraProps } from "@chakra-ui/react";
import React, { useEffect } from "react";
import { useGetKnowledgeHistory } from "services/MiscService";
import { echarts, ECLineOption } from "configs";
import numeral from "numeral";

interface RecordCountGrowthChartProps extends HTMLChakraProps<"div"> {
  days?: number;
}

const RecordCountGrowthChart = ({
  days = 12,
  ...props
}: RecordCountGrowthChartProps) => {
  const chartRef = React.useRef<HTMLDivElement>(null);
  const getKnowledgeHistory = useGetKnowledgeHistory({
    variables: { days },
  });
  useEffect(() => {
    if (chartRef.current === null || getKnowledgeHistory.data === undefined) {
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
        data: ["Entity", "Relationship", "Property"],
      },
      xAxis: {
        type: "category",
        data: getKnowledgeHistory.data.vertices.map((item) => item.date),
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
          name: "Entity",
          type: "bar",
          stack: "Knowledge",
          data: getKnowledgeHistory.data.vertices.map((item) => item.value),
        },
        {
          name: "Relationship",
          type: "bar",
          stack: "Knowledge",
          data: getKnowledgeHistory.data.edges.map((item) => item.value),
        },
        {
          name: "Property",
          type: "bar",
          stack: "Knowledge",
          data: getKnowledgeHistory.data.properties.map((item) => item.value),
        },
      ],
    };
    chart.setOption(options);
  }, [chartRef, getKnowledgeHistory.data]);
  return <Box ref={chartRef} {...props}></Box>;
};

export default RecordCountGrowthChart;
