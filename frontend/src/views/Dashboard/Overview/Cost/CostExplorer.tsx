import {
  Box,
  Divider,
  Heading,
  HStack,
  HTMLChakraProps,
  Spacer,
  Text,
  VStack,
} from "@chakra-ui/react";
import { useEffect, useRef } from "react";
import { useGetAccountCost, UseGetAccountCostResponse } from "services";
import { echarts, ECLineOption } from "configs";
import numeral from "numeral";

const CostExplorer = (props: HTMLChakraProps<"div">) => {
  const getCostDetails = useGetAccountCost();
  return (
    <HStack {...props} gap={0}>
      <CostTrendChart
        series={getCostDetails.data?.series}
        flex={3}
        h={"full"}
      />
      <CostUsages
        usages={getCostDetails.data?.usages}
        flex={1}
        h={"full"}
        py={2}
      />
    </HStack>
  );
};

interface CostUsagesProps extends HTMLChakraProps<"div"> {
  usages?: UseGetAccountCostResponse["usages"];
}

const CostUsages = ({ usages = [], ...rest }: CostUsagesProps) => {
  return (
    <VStack
      {...rest}
      alignItems={"stretch"}
      justifyContent={"flex-start"}
      pr={4}
    >
      <Heading size={"md"} py={2}>
        Costs for current month
      </Heading>
      {usages.map((usage, i) => {
        return (
          <>
            <HStack key={usage.label}>
              <Text>{usage.label}</Text>
              <Spacer />
              <Text>{numeral(usage.amount).format("0[.]00a")}</Text>
            </HStack>
            {i < usages.length - 1 && <Divider color={"gray.300"} />}
          </>
        );
      })}
    </VStack>
  );
};

interface CostTrendChartProps extends HTMLChakraProps<"div"> {
  series?: UseGetAccountCostResponse["series"];
}

const CostTrendChart = ({ series = [], ...rest }: CostTrendChartProps) => {
  const chartRef = useRef<HTMLDivElement>(null);
  useEffect(() => {
    if (chartRef.current === null) {
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
        data: ["Entity", "Relationship", "Property"],
      },
      xAxis: {
        type: "category",
        data: series.map((item) => item.date),
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
          name: "Cost (USD)",
          type: "bar",
          data: series.map((item) => item.value).reverse(),
          tooltip: {
            valueFormatter: (value) => {
              return numeral(value).format("0,0[.]00a");
            },
          },
        },
      ],
    };
    chart.setOption(options);
  }, [chartRef, series]);
  return <Box ref={chartRef} {...rest} />;
};

export default CostExplorer;
