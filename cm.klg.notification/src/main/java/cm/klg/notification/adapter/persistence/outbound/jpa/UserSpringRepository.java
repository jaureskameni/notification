package cm.klg.notification.adapter.persistence.outbound.jpa;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserSpringRepository extends JpaRepository<UserJpa, UUID> {

  @Modifying
  @Query(
      value =
          "INSERT INTO t_user (c_id, c_firstname, c_lastname, c_email,"
              + " c_phone_number, c_created_at) VALUES (:id, :firstname, :lastname,"
              + " :email, :phoneNumber, :createdAt) ON CONFLICT (c_id) DO NOTHING",
      nativeQuery = true)
  int insertIfAbsent(
      @Param("id") String id,
      @Param("firstname") String firstname,
      @Param("lastname") String lastname,
      @Param("email") String email,
      @Param("phoneNumber") String phoneNumber,
      @Param("createdAt") LocalDateTime createdAt);
}
