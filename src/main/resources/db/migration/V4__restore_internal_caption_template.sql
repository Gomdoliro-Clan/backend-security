UPDATE presentation_profile
SET settings = jsonb_set(
  settings,
  '{totalCaption}',
  to_jsonb('#{#supplier}: #{#total} #{#currency}'::text)
)
WHERE settings->>'totalCaption' = ('$' || '{supplier}: $' || '{total} $' || '{currency}');
