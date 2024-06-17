insert into t_user(username, password) values ('sam', '$2a$12$Bq1X9tYfci8243RoBVbLPumlQh4zzgohynoE/g4t8hFlEpy6y6leW'),
                                              ('max', '$2a$12$Uul0u6tluYuAXem/lbU0me0RRHMUoo4AeZ4.CkVivGzCy7DQzGy8G');

insert into t_user_authority(id_user, authority) values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');