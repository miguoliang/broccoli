package broccoli.models;

import java.io.Serializable;

/**
 * The {@link EdgeId}.
 */

public record EdgeId(int inVertex, int outVertex, String name, String scope)
    implements Serializable {
}
