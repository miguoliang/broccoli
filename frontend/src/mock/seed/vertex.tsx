import { Vertex } from "models";
import { faker } from "@faker-js/faker";
import times from "lodash-es/times";
import md5 from "md5";

export const vertices: Vertex[] = times(100, () => {
  const name = faker.lorem.words(2);
  const type = faker.lorem.word();
  const id = md5(name + type);
  return {
    id,
    name,
    type,
    status: "normal",
    createdAt: faker.date.past().toISOString(),
    updatedAt: faker.date.past().toISOString(),
  } as Vertex;
});
