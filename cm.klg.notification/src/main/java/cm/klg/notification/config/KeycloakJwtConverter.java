package cm.klg.notification.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    Collection<GrantedAuthority> authorities =
        new ArrayList<>(jwtGrantedAuthoritiesConverter.convert(jwt));

    Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
    if (realmAccess != null) {
      List<String> roles = List.copyOf(realmAccess.getOrDefault("roles", List.of()));
      roles.stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
          .forEach(authorities::add);
    }

    String principal = jwt.getSubject();
    return new JwtAuthenticationToken(jwt, authorities, principal);
  }
}
