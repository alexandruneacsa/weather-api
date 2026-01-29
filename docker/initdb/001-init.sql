-- Optional init SQL: creates a simple table for quick manual verification
CREATE TABLE IF NOT EXISTS sample_init (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
