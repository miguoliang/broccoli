import { useSearchVertices } from "../gens/backend/api";

const Home = () => {
  const { data, isSuccess } = useSearchVertices({
    q: "test",
  });

  return (
    isSuccess && (
      <ul>{data?.data.content.map((it) => <li key={it.id}>{it.name}</li>)}</ul>
    )
  );
};

export default Home;
