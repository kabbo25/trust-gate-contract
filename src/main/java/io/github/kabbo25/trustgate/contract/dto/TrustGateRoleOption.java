package io.github.kabbo25.trustgate.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Generic role option for multirole selection during login.
 *
 * <p>This DTO is intentionally domain-agnostic. The SSO server renders
 * {@code displayName}, {@code description}, and {@code tags} in the UI —
 * it never interprets them. Each provider maps its own domain concepts
 * into these generic display fields.</p>
 *
 * <p>The {@code claims} map is opaque to the SSO server. When the user
 * selects a role, this map is stored in the session and later merged
 * into the JWT access token by {@code TrustGateTokenCustomizer}.</p>
 *
 * <p>Implements {@link Serializable} because instances are cached in
 * the {@link jakarta.servlet.http.HttpSession}, which is backed by Redis.</p>
 *
 * <h3>Example mappings by provider:</h3>
 * <table>
 *   <tr><th>Provider</th><th>displayName</th><th>description</th><th>tags</th></tr>
 *   <tr><td>IPEMIS</td><td>"Head Teacher"</td><td>"Gazipur, Kaliakair Upazila"</td><td>["In-Charge"]</td></tr>
 *   <tr><td>Acme Corp</td><td>"Project Manager"</td><td>"Engineering Team"</td><td>["Lead"]</td></tr>
 * </table>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrustGateRoleOption implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Unique role identifier. Used as the form value during selection.
     * String type for maximum provider flexibility (numeric IDs, UUIDs, etc.).
     */
    private String id;

    /**
     * Human-readable role name for display.
     * Example: "Head Teacher", "District Education Officer", "Project Manager"
     */
    private String displayName;

    /**
     * Localized role name. Null if provider doesn't support localization.
     */
    private String displayNameLocal;

    /**
     * Additional context shown below the role name.
     * Example: "Gazipur, Kaliakair Upazila" or "Engineering Team Alpha"
     */
    private String description;

    /**
     * Localized description. Null if provider doesn't support localization.
     */
    private String descriptionLocal;

    /**
     * Whether this is the user's primary/default role.
     * Rendered with a "Main Role" badge in the UI.
     */
    private boolean primaryRole;

    /**
     * Arbitrary display tags/badges for the role.
     * Example: ["Current Role"], ["In-Charge"], ["Temporary"]
     * Rendered as small badges next to the role in the UI.
     */
    @Builder.Default
    private List<String> tags = Collections.emptyList();

    /**
     * Localized tags/badges. Null if provider doesn't support localization.
     * When present and the UI locale matches, displayed instead of {@code tags}.
     */
    @Builder.Default
    private List<String> tagsLocal = Collections.emptyList();

    /**
     * Opaque claim data for JWT embedding.
     * When this role is selected, this entire map replaces the default
     * {@code userRole} value inside the {@code user} custom claim.
     * The SSO server never reads or interprets this data.
     */
    @Builder.Default
    private Map<String, Object> claims = Collections.emptyMap();
}
