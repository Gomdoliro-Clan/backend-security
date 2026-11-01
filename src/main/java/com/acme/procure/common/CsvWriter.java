package com.acme.procure.common;

import java.util.List;
import java.util.Map;

public final class CsvWriter {
  private CsvWriter() {}

  public static String write(List<String> columns, List<Map<String, Object>> rows) {
    StringBuilder out = new StringBuilder(String.join(",", columns)).append("\r\n");
    for (Map<String, Object> row : rows) {
      out.append(
              columns.stream()
                  .map(c -> cell(row.get(c)))
                  .collect(java.util.stream.Collectors.joining(",")))
          .append("\r\n");
    }
    return out.toString();
  }

  private static String cell(Object value) {
    return "\"" + String.valueOf(value == null ? "" : value).replace("\"", "\"\"") + "\"";
  }
}
