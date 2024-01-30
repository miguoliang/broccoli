// calculate sha512 hex string
import { NodeType } from "../components/ui/blocks";

async function sha512(str: string) {
  // We transform the string into an arraybuffer.
  const buffer = new TextEncoder().encode(str);
  const hash = await crypto.subtle.digest("SHA-512", buffer);
  return hex(hash);
}

export function getVertexId(type: NodeType, name: string) {
  return sha512(`${type}_${name}`);
}

function hex(buffer: ArrayBuffer) {
  return [].slice
    .call(new Uint8Array(buffer))
    .map((x: number) => x.toString(16).padStart(2, "0"))
    .join("");
}
