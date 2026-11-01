package com.acme.procure.worker;

import java.util.Map;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class CaptionRenderer {
  private final SpelExpressionParser expressions = new SpelExpressionParser();

  public String render(String template, Map<String, Object> variables) {
    StandardEvaluationContext context = new StandardEvaluationContext();
    variables.forEach(context::setVariable);
    return expressions
        .parseExpression(template, new TemplateParserContext())
        .getValue(context, String.class);
  }
}
