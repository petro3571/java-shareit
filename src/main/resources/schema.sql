CREATE TABLE IF NOT EXISTS users (
          user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          name VARCHAR(40),
          email VARCHAR(255)
        );

        CREATE TABLE IF NOT EXISTS booking_status (
          id INT PRIMARY KEY,
          name VARCHAR(40) NOT NULL
        );

        INSERT INTO booking_status (id, name) VALUES
          (1, 'WAITING'),
          (2, 'APPROVED'),
          (3, 'REJECTED'),
          (4, 'CANCELED')
        ON CONFLICT (id) DO NOTHING;

        CREATE TABLE IF NOT EXISTS item_request (
          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          description VARCHAR(255) NOT NULL,
          user_id INTEGER REFERENCES users ON DELETE CASCADE,
          created DATE NOT NULL
        );

        CREATE TABLE IF NOT EXISTS items (
          item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          name VARCHAR(40),
          description VARCHAR(255),
          available BOOLEAN,
          owner_id INTEGER REFERENCES users ON DELETE CASCADE,
          request_id INTEGER REFERENCES item_request ON DELETE CASCADE
        );

        CREATE TABLE IF NOT EXISTS booking (
          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          start_date TIMESTAMP WITHOUT TIME ZONE,
          end_date TIMESTAMP WITHOUT TIME ZONE,
          item_id INTEGER REFERENCES items ON DELETE CASCADE,
          booker_id INTEGER REFERENCES users ON DELETE CASCADE,
          status varchar(50)
        );

        CREATE TABLE IF NOT EXISTS comments (
          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
          item_id INTEGER REFERENCES items ON DELETE CASCADE,
          author_id INTEGER REFERENCES users ON DELETE CASCADE,
          text VARCHAR(255),
          created TIMESTAMP WITHOUT TIME ZONE
        );