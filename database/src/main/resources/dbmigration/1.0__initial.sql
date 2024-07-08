-- apply changes
create table dtelegram_user (
  id                            bigint generated by default as identity not null,
  user_id                       bigint not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint uq_dtelegram_user_user_id unique (user_id),
  constraint pk_dtelegram_user primary key (id)
);

create table dwhatsapp_session (
  id                            bigint generated by default as identity not null,
  session_unique_id             uuid not null,
  user_id                       bigint not null,
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint uq_dwhatsapp_session_session_unique_id unique (session_unique_id),
  constraint pk_dwhatsapp_session primary key (id)
);

-- foreign keys and indices
create index ix_dwhatsapp_session_user_id on dwhatsapp_session (user_id);
alter table dwhatsapp_session add constraint fk_dwhatsapp_session_user_id foreign key (user_id) references dtelegram_user (id) on delete restrict on update restrict;
