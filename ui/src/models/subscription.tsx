export interface Subscription {
  email: string;
  subscriptionId: string;
  planId: string;
  planName: string;
  planDescription: string;
  planAmount: number;
  startTime: string;
  endTime: string;
  status: string;
}
