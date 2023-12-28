package broccoli.model.identity.http.response;

/**
 * The {@link QueryUserResponse} class.
 */
public record QueryUserResponse(String id, String username, String email, Long createdTime,
                                String firstName, String lastName,
                                boolean enabled) {
}
