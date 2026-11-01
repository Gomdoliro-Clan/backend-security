package com.acme.procure.bootstrap;

import com.acme.procure.common.JsonCodec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "lab.seed", havingValue = "true")
public class DemoData implements ApplicationRunner {
  private final JdbcTemplate jdbc;
  private final PasswordEncoder passwords;
  private final JsonCodec json;
  private final String managerPassword;

  public DemoData(
      JdbcTemplate jdbc,
      PasswordEncoder passwords,
      JsonCodec json,
      @Value("${lab.manager-password}") String managerPassword) {
    this.jdbc = jdbc;
    this.passwords = passwords;
    this.json = json;
    this.managerPassword = managerPassword;
  }

  private UUID id(String key) {
    return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    if (jdbc.queryForObject("SELECT count(*) FROM organization", Integer.class) > 0) return;
    for (String tenant : List.of("hanul", "mirae")) {
      UUID org = id(tenant);
      jdbc.update(
          "INSERT INTO organization VALUES (?,?,?)",
          org,
          tenant.equals("hanul") ? "하늘 테크" : "미래 스튜디오",
          "KRW");
      for (String role : List.of("reader", "analyst", "buyer", "manager")) {
        String password =
            tenant.equals("hanul")
                ? (role.equals("manager")
                    ? managerPassword
                    : Character.toUpperCase(role.charAt(0)) + role.substring(1) + "-lab-2026!")
                : UUID.randomUUID().toString();
        jdbc.update(
            "INSERT INTO app_user(id,organization_id,email,display_name,password_hash,role) VALUES"
                + " (?,?,?,?,?,?)",
            id(tenant + role),
            org,
            role + "@" + tenant + ".example",
            tenant + " " + role,
            passwords.encode(password),
            role.toUpperCase(Locale.ROOT));
      }
      for (int i = 1; i <= 16; i++) {
        UUID supplier = id(tenant + "supplier" + i);
        jdbc.update(
            "INSERT INTO supplier VALUES (?,?,?,?,?,?)",
            supplier,
            org,
            "Supply Partner " + i,
            "sales" + i + "@" + tenant + ".example",
            i % 2 == 0 ? "OFFICE" : "HARDWARE",
            "ACTIVE");
        jdbc.update(
            "INSERT INTO catalog_item VALUES (?,?,?,?,?,?)",
            id(tenant + "item" + i),
            org,
            "SKU-" + i,
            "Office item " + i,
            BigDecimal.valueOf(i * 3500L),
            100 + i);
        jdbc.update(
            "INSERT INTO"
                + " purchase_order(id,organization_id,supplier_id,created_by,title,department,amount,status)"
                + " VALUES (?,?,?,?,?,?,?,'SUBMITTED')",
            id(tenant + "purchase" + i),
            org,
            supplier,
            id(tenant + "buyer"),
            "Quarterly supply " + i,
            i % 2 == 0 ? "Engineering" : "Operations",
            BigDecimal.valueOf(i * 15000L));
      }
      for (int i = 1; i <= 48; i++) {
        jdbc.update(
            "INSERT INTO invoice(id,organization_id,number,department,total,status,issued_on,memo)"
                + " VALUES (?,?,?,?,?,?,current_date-(?::integer),'Synthetic classroom record')",
            id(tenant + "invoice" + i),
            org,
            tenant.toUpperCase(Locale.ROOT) + "-INV-" + i,
            i % 3 == 0 ? "Research" : i % 2 == 0 ? "Engineering" : "Operations",
            BigDecimal.valueOf(i * 8500L),
            i % 4 == 0 ? "RECONCILED" : "ISSUED",
            i);
      }
      for (int i = 1; i <= 8; i++) {
        jdbc.update(
            "INSERT INTO document(organization_id,title,kind,body) VALUES (?,?,?,?)",
            org,
            tenant + " 내부 구매 메모 " + i,
            "MEMO",
            tenant + " 팀의 내부 일정 및 사무용품 검토 메모 " + i + ". 교육용 합성 데이터.");
      }
      jdbc.update(
          "INSERT INTO rfq VALUES (?,?,?,?,?,'OPEN',current_date+365)",
          id(tenant + "rfq"),
          org,
          tenant + "-office-2026",
          "Office equipment renewal",
          "KRW");
      jdbc.update(
          "INSERT INTO presentation_profile VALUES (?,?::jsonb)",
          org,
          json.write(
              Map.of(
                  "currency",
                  "KRW",
                  "locale",
                  "ko-KR",
                  "totalCaption",
                  "#{#supplier}: #{#total} #{#currency}")));
    }
  }
}
