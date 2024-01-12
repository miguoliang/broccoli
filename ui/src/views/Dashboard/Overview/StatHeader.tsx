import {
  useGetEdgesStat,
  useGetPropertiesStat,
  useGetVerticesStat,
} from "services";
import { useMemo } from "react";
import first from "lodash-es/first";
import {
  SkeletonText,
  Stat,
  StatArrow,
  StatGroup,
  StatHelpText,
  StatLabel,
  StatNumber,
} from "@chakra-ui/react";
import numeral from "numeral";

const StatHeader = () => {
  const verticesStatQuery = useGetVerticesStat();
  const edgesStatQuery = useGetEdgesStat();
  const propertiesStatQuery = useGetPropertiesStat();

  const { knowledgeStat, verticesStat, edgesStat, propertiesStat } =
    useMemo(() => {
      const verticesStat = first(verticesStatQuery.data);
      const edgesStat = first(edgesStatQuery.data);
      const propertiesStat = first(propertiesStatQuery.data);

      if (!verticesStat || !edgesStat || !propertiesStat) {
        return { verticesStat, edgesStat, propertiesStat };
      }

      const knowledgeStatValue =
        verticesStat.value + edgesStat.value + propertiesStat.value;
      const knowledgeStatChanged =
        verticesStat.changed + edgesStat.changed + propertiesStat.changed;
      const knowledgeStatPercentage =
        (knowledgeStatChanged / (knowledgeStatValue - knowledgeStatChanged)) *
        100;
      const knowledgeStat = {
        label: verticesStat.label,
        value: knowledgeStatValue,
        changed: knowledgeStatChanged,
        percentage: knowledgeStatPercentage,
      };
      return { knowledgeStat, verticesStat, edgesStat, propertiesStat };
    }, [verticesStatQuery.data, edgesStatQuery.data, propertiesStatQuery.data]);

  const isKnowledgeStatLoading =
    verticesStatQuery.isLoading ||
    edgesStatQuery.isLoading ||
    propertiesStatQuery.isLoading;
  const isKnowledgeStatError =
    verticesStatQuery.isError ||
    edgesStatQuery.isError ||
    propertiesStatQuery.isError;

  return (
    <StatGroup h={"full"} gap={5}>
      <StatCell
        label={"Knowledge"}
        value={knowledgeStat?.value}
        percentage={knowledgeStat?.percentage}
        isLoading={isKnowledgeStatLoading}
        isError={isKnowledgeStatError}
      />
      <StatCell
        label={"Entities"}
        value={verticesStat?.value}
        percentage={verticesStat?.percentage}
        isLoading={verticesStatQuery.isLoading}
        isError={verticesStatQuery.isError}
      />
      <StatCell
        label={"Relationships"}
        value={edgesStat?.value}
        percentage={edgesStat?.percentage}
        isLoading={edgesStatQuery.isLoading}
        isError={edgesStatQuery.isError}
      />
      <StatCell
        label={"Properties"}
        value={propertiesStat?.value}
        percentage={propertiesStat?.percentage}
        isLoading={propertiesStatQuery.isLoading}
        isError={propertiesStatQuery.isError}
      />
    </StatGroup>
  );
};

interface StatCellProps {
  label: string;
  value?: number;
  percentage?: number;
  isLoading?: boolean;
  isError?: boolean;
}

const StatCell = ({
  label,
  value = 0,
  percentage = 0,
  isLoading = false,
}: StatCellProps) => {
  return (
    <Stat p={4} borderRadius={"xl"} borderWidth={1}>
      <StatLabel>
        <SkeletonText isLoaded={!isLoading}>{label}</SkeletonText>
      </StatLabel>
      <StatNumber>
        <SkeletonText isLoaded={!isLoading}>
          {numeral(value).format("0,0")}
        </SkeletonText>
      </StatNumber>
      <SkeletonText isLoaded={!isLoading}>
        {value !== 0 && (
          <StatHelpText>
            <StatArrow type={percentage > 0 ? "increase" : "decrease"} />
            {numeral(percentage).format("0,0.00")}%
          </StatHelpText>
        )}
      </SkeletonText>
    </Stat>
  );
};

export default StatHeader;
