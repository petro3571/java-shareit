DELETE FROM users;
DELETE FROM items;
DELETE FROM booking;
DELETE FROM comments;

INSERT INTO booking_status (id, name) VALUES
          (1, 'WAITING'),
          (2, 'APPROVED'),
          (3, 'REJECTED'),
          (4, 'CANCELED')
        ON CONFLICT (id) DO NOTHING;