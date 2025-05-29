INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO users (id, username, email, password, enabled, created_at)
VALUES (
    1,
    'admin',
    'admin@admin.com',
    '$2a$12$/cK1bn///ydauSMCJ4uFQuKxGrhRTxef94hQJm7ImMmraJJZVXxKa', -- bcrypt: zaq1@WSX
    true,
    NOW()
);

INSERT INTO users (id, username, email, password, enabled, created_at)
VALUES (
    2,
    'user',
    'user@user.com',
    '$2a$12$/cK1bn///ydauSMCJ4uFQuKxGrhRTxef94hQJm7ImMmraJJZVXxKa', -- bcrypt: zaq1@WSX
    true,
    NOW()
);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);
