import { DataSource } from "models";
import { faker } from "@faker-js/faker";
import times from "lodash-es/times";
import md5 from "md5";

export const dataSources: DataSource[] = times(100, () => {
  const title = faker.lorem.sentence();
  const id = md5(title);
  return {
    id,
    title: title,
    source: faker.internet.url(),
    sourceType: "url",
    contentType: "text",
    content: faker.lorem.paragraph(),
    createdAt: faker.date.past().toISOString(),
    updatedAt: faker.date.past().toISOString(),
  };
});
