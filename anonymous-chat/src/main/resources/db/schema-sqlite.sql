-- SQLite schema for Anonymous Chat Application

-- Client table (stores unique client identifiers)
CREATE TABLE IF NOT EXISTS anonymous_client (
    client_id VARCHAR(64) PRIMARY KEY,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Virtual Account table
CREATE TABLE IF NOT EXISTS anonymous_account (
    id BIGINT PRIMARY KEY,
    client_id VARCHAR(64) NOT NULL,
    nickname VARCHAR(32) NOT NULL,
    gender INTEGER DEFAULT 0,
    age INTEGER DEFAULT 18,
    region VARCHAR(64) DEFAULT '未知地区',
    signature VARCHAR(128),
    avatar VARCHAR(255) NOT NULL,
    is_deleted INTEGER DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES anonymous_client(client_id)
);

CREATE INDEX IF NOT EXISTS idx_account_client_id ON anonymous_account(client_id);

-- Match records table
CREATE TABLE IF NOT EXISTS chat_match (
    id BIGINT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    status INTEGER NOT NULL,
    gender_preference INTEGER DEFAULT 0,
    min_age INTEGER DEFAULT 18,
    max_age INTEGER DEFAULT 100,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_match_account_id ON chat_match(account_id);
CREATE INDEX IF NOT EXISTS idx_match_target_id ON chat_match(target_id);

-- Chat messages table
CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT PRIMARY KEY,
    send_id BIGINT NOT NULL,
    receive_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(16) DEFAULT 'text',
    is_read INTEGER DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_message_send_receive ON chat_message(send_id, receive_id);
CREATE INDEX IF NOT EXISTS idx_message_create_time ON chat_message(create_time);

-- Contacts table
CREATE TABLE IF NOT EXISTS chat_contact (
    id BIGINT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    remark VARCHAR(32),
    black_status INTEGER DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(account_id, friend_id)
);

CREATE INDEX IF NOT EXISTS idx_contact_account_id ON chat_contact(account_id);
CREATE INDEX IF NOT EXISTS idx_contact_friend_id ON chat_contact(friend_id);
