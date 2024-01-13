import * as echarts from "echarts/core";
import { ComposeOption } from "echarts/core";
import {
  GridComponent,
  GridComponentOption,
  LegendComponent,
  LegendComponentOption,
  TooltipComponent,
} from "echarts/components";
import { CanvasRenderer } from "echarts/renderers";
import {
  BarChart,
  BarSeriesOption,
  LineChart,
  LineSeriesOption,
  PieChart,
  PieSeriesOption,
} from "echarts/charts";

echarts.use([
  LineChart,
  BarChart,
  PieChart,
  LegendComponent,
  CanvasRenderer,
  GridComponent,
  TooltipComponent,
]);

export type ECLineOption = ComposeOption<
  | LineSeriesOption
  | BarSeriesOption
  | PieSeriesOption
  | LegendComponentOption
  | GridComponentOption
>;

export default echarts;
