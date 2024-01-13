import { describe, expect, test } from "vitest";

describe("hello, generic", () => {
  test("keyof as a type", () => {
    type Person = { name: string; age: number };
    const person: Person = { name: "Alice", age: 20 };
    const getProperty = (obj: Person, key: keyof Person) => obj[key];
    expect(getProperty(person, "name")).toBe("Alice");
    expect(getProperty(person, "age")).toBe(20);
    // 这里会报错，因为 'foo' 不是 Person 接口中的有效属性名称
    // expect(getProperty(person, "foo"));
  });
  test("keyof string & number", () => {
    type Arrayish = { [n: number]: unknown };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type A = keyof Arrayish; // number
    type Mapish = { [k: string]: boolean };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type M = keyof Mapish; // string | number
  });
  test("keyof type", () => {
    type A = {
      x: number;
      y: number;
    };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type A2 = keyof A;
  });
  test("ReturnType usage", () => {
    type Predicate = (x: unknown) => boolean;
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type K = ReturnType<Predicate>;
  });
  test("Indexed access types (object)", () => {
    type Person = { age: number; name: string; alive: boolean };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type T1 = Person[keyof Person];
  });
  test("Indexed access types (array)", () => {
    const MyArray = [
      { name: "Alice", age: 15 },
      { name: "Bob", age: 23 },
      { name: "Eve", age: 38 },
    ];

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type Person = (typeof MyArray)[number];
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type Age = (typeof MyArray)[number]["age"];
  });
  test("Conditional types (function)", () => {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type T1 = ReturnType<typeof stringOrNum>;
  });
  test("Non distributive conditional types", () => {
    type ToArrayNonDist<Type> = [Type] extends [any] ? Type[] : never;
    // 'StrArrOrNumArr' is no longer a union.
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type StrArrOrNumArr = ToArrayNonDist<string | number>;
  });
  test("Distributive conditional types", () => {
    type ToArray<Type> = Type extends any ? Type[] : never;
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type StrArrOrNumArr = ToArray<string | number>;
  });
  test("Mapping types, modify value types", () => {
    type OptionsFlags<Type> = {
      [Property in keyof Type]: boolean;
    };
    type FeatureFlags = {
      darkMode: () => void;
      newUserProfile: () => void;
    };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type FeatureOptions = OptionsFlags<FeatureFlags>;
  });
  test("Mapping types, removes 'readonly' attributes from a type's properties", () => {
    // Removes 'readonly' attributes from a type's properties
    type CreateMutable<Type> = {
      -readonly [Property in keyof Type]: Type[Property];
    };
    type LockedAccount = {
      readonly id: string;
      readonly name: string;
    };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type UnlockedAccount = CreateMutable<LockedAccount>;
  });
  test("Mapping types, removes 'optional' attributes from a type's properties", () => {
    type Concrete<Type> = {
      [Property in keyof Type]-?: Type[Property];
    };
    type MaybeUser = {
      id: string;
      name?: string;
      age?: number;
    };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type User = Concrete<MaybeUser>;
  });
  test("Mapping types, with template literal types", () => {
    type Getters<Type> = {
      [Property in keyof Type as `get${Capitalize<
        string & Property
      >}`]: () => Type[Property];
    };

    interface Person {
      name: string;
      age: number;
      location: string;
    }

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type LazyPerson = Getters<Person>;
  });
  test("Mapping types, remove the 'kind' property", () => {
    type RemoveKindField<Type> = {
      [Property in keyof Type as Exclude<Property, "kind">]: Type[Property];
    };

    interface Circle {
      kind: "circle";
      radius: number;
    }

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type KindlessCircle = RemoveKindField<Circle>;
  });
  test("Mapping types, map over arbitrary unions", () => {
    type EventConfig<Events extends { kind: string }> = {
      [E in Events as E["kind"]]: (event: E) => void;
    };

    type SquareEvent = { kind: "square"; x: number; y: number };
    type CircleEvent = { kind: "circle"; radius: number };

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type Config = EventConfig<SquareEvent | CircleEvent>;
  });
  test("Mapping types using a conditional type which returns either a true or false depending on whether an object has the property pii set to the literal true", () => {
    type ExtractPII<Type> = {
      [Property in keyof Type]: Type[Property] extends { pii: true }
        ? true
        : false;
    };

    type DBFields = {
      id: { format: "incrementing" };
      name: { type: string; pii: true };
    };

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    type ObjectsNeedingGDPRDeletion = ExtractPII<DBFields>;
  });
});

declare function stringOrNum(x: string): number;
declare function stringOrNum(x: number): string;
declare function stringOrNum(x: string | number): string | number;

describe("fundamental", () => {
  test("default values", () => {
    interface Person {
      name?: string;
      age?: number;
      gender?: "male" | "female";
    }

    const person: Person = { name: "Alice", age: 20 };
    const { name = "Jack", age, gender = "male" } = person;
    expect(name).toBe("Alice");
    expect(gender).toBe("male");
    expect(age).toBe(20);
  });
  test("match enum type", () => {
    enum Gender {
      Male = "male",
      Female = "female",
    }

    interface Person {
      name: string;
      gender: Gender;
    }

    const person: Person = { name: "Alice", gender: Gender.Female };
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { name, gender = Gender.Male } = person;
    expect(gender).toBe(Gender.Female);
  });
  test("literal type compare", () => {
    const name: "name" | "nick" = "name";
    expect(false).toBe((name as string) === "age");
    // expect(false).toBe(name === "nick"); // compile error
  });
  test("object and its keys", () => {
    const foo: { [_: string]: string | undefined | null } = {};
    expect(foo["abc"]).toBeUndefined();
    expect(!!foo["abc"]).toBeFalsy();
  });
  test("null ,undefined and zero in if conditional statement", () => {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    expect(undefined ? 1 : 0).toBe(0);
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    expect(null ? 1 : 0).toBe(0);
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    expect(0 ? 1 : 0).toBe(0);
  });
});
