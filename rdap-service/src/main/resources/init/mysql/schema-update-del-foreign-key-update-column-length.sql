ALTER TABLE REL_SECUREDNS_DSKEY  DROP FOREIGN KEY  `FK_REL_SECUREDNS_DSKEY_SECUREDNS_ID`; 
ALTER TABLE REL_DOMAIN_VARIANT  DROP FOREIGN KEY  `FK_REL_DOMAIN_VARIANT_DOMAIN_ID`;
ALTER TABLE REL_DOMAIN_VARIANT  DROP FOREIGN KEY `FK_REL_DOMAIN_VARIANT_VARIANT_ID`;
ALTER TABLE RDAP_AUTNUM_STATUS  DROP FOREIGN KEY  `FK_RDAP_AUTNUM_STATUS_AS_ID`;
ALTER TABLE RDAP_DOMAIN_STATUS  DROP FOREIGN KEY  `FK_RDAP_DOMAIN_STATUS_DOMAIN_ID`;
ALTER TABLE RDAP_ENTITY_STATUS  DROP FOREIGN KEY  `FK_RDAP_ENTITY_STATUS_ENTITY_ID`;
ALTER TABLE RDAP_IP_STATUS  DROP FOREIGN KEY  `FK_RDAP_IP_STATUS_IP_ID`;
ALTER TABLE RDAP_LINK_HREFLANG DROP FOREIGN KEY  `FK_RDAP_LINK_HREFLANG_LINK_ID`;
ALTER TABLE RDAP_NAMESERVER_IP DROP FOREIGN KEY  `FK_RDAP_NAMESERVER_IP_NAMESERVER_ID`;
ALTER TABLE  RDAP_NAMESERVER_STATUS  DROP FOREIGN KEY  `FK_RDAP_NAMESERVER_STATUS_NAMESERVER_ID`;
ALTER TABLE  RDAP_NOTICE_DESCRIPTION  DROP FOREIGN KEY  `FK_RDAP_NOTICE_DESCRIPTION_NOTICE_ID`;
ALTER TABLE  RDAP_VCARD_ADR  DROP FOREIGN KEY  `FK_RDAP_ENTITY_ADR_ENTITY_ID`;
ALTER TABLE  RDAP_VCARD_TEL  DROP FOREIGN KEY  `FK_RDAP_ENTITY_TEL_ENTITY_ID`;
ALTER TABLE  REL_DOMAIN_NAMESERVER  DROP FOREIGN KEY  `FK_REL_DOMAIN_NAMESERVER_NAMESERVER_ID`;
ALTER TABLE  REL_ENTITY_REGISTRATION DROP FOREIGN KEY  `FK_REL_ENTITY_REGISTRATION_ENTITY_ID`;
ALTER TABLE  REL_EVENT_REGISTRATION  DROP FOREIGN KEY  `FK_REL_EVENT_REGISTRATION_EVENT_ID`;
ALTER TABLE  REL_LINK_OBJECT  DROP FOREIGN KEY  `FK_REL_LINK_OBJECT_LINK_ID`;
ALTER TABLE  REL_NOTICE_REGISTRATION  DROP FOREIGN KEY  `FK_REL_NOTICE_REGISTRATION_NOTICE_ID`;
ALTER TABLE  REL_PUBLICID_REGISTRATION  DROP FOREIGN KEY  `FK_REL_PUBLICID_REGISTRATION_PUBLIC_ID`;
ALTER TABLE RDAP_IDENTITY_USER_REL_ROLE  DROP FOREIGN KEY  `RDAP_IDENTITY_USER_REL_ROLE_ibfk_1`;

ALTER TABLE RDAP_LINK MODIFY REL VARCHAR(255);
ALTER TABLE RDAP_LINK MODIFY MEDIA VARCHAR(255);
ALTER TABLE RDAP_LINK MODIFY TYPE VARCHAR(255);
ALTER TABLE RDAP_LINK MODIFY TITLE VARCHAR(255);
ALTER TABLE RDAP_LINK MODIFY VALUE VARCHAR(2048);
ALTER TABLE RDAP_LINK MODIFY HREF VARCHAR(2048);
ALTER TABLE RDAP_VARIANT MODIFY IDNTABLE VARCHAR(255);
ALTER TABLE REL_DOMAIN_VARIANT MODIFY VARIANT_TYPE VARCHAR(255);
ALTER TABLE RDAP_KEYDATA MODIFY PUBLIC_KEY VARCHAR(2048);
ALTER TABLE RDAP_PUBLICID MODIFY IDENTIFIER VARCHAR(255);
ALTER TABLE RDAP_PUBLICID MODIFY TYPE VARCHAR(255);
ALTER TABLE RDAP_NOTICE MODIFY TITLE VARCHAR(255);
ALTER TABLE RDAP_EVENT MODIFY EVENT_ACTION VARCHAR(255);
ALTER TABLE RDAP_EVENT MODIFY EVENT_ACTOR VARCHAR(255);

ALTER TABLE RDAP_ENTITY MODIFY KIND VARCHAR(255);
ALTER TABLE RDAP_ENTITY MODIFY FN VARCHAR(255);
ALTER TABLE RDAP_ENTITY MODIFY EMAIL VARCHAR(255);
ALTER TABLE RDAP_ENTITY MODIFY TITLE VARCHAR(255);
ALTER TABLE RDAP_ENTITY MODIFY ORG VARCHAR(255);

ALTER TABLE RDAP_IP MODIFY NAME VARCHAR(255);
ALTER TABLE RDAP_IP MODIFY TYPE VARCHAR(255);
ALTER TABLE RDAP_IP MODIFY COUNTRY VARCHAR(255);
ALTER TABLE RDAP_IP MODIFY PARENT_HANDLE VARCHAR(255);
ALTER TABLE RDAP_IP MODIFY CIDR VARCHAR(255);

ALTER TABLE RDAP_VCARD_ADR MODIFY POST_BOX  VARCHAR(255);
ALTER TABLE RDAP_VCARD_ADR MODIFY EXT_ADR VARCHAR(255);
ALTER TABLE RDAP_VCARD_ADR MODIFY STREET VARCHAR(255);
ALTER TABLE RDAP_VCARD_ADR MODIFY CITY VARCHAR(255);
ALTER TABLE RDAP_VCARD_ADR MODIFY SP VARCHAR(255);
ALTER TABLE RDAP_VCARD_ADR MODIFY POSTAL_CODE VARCHAR(255);
ALTER TABLE RDAP_VCARD_ADR MODIFY COUNTRY VARCHAR(255);
ALTER TABLE RDAP_VCARD_ADR MODIFY TYPE VARCHAR(255);

ALTER TABLE RDAP_VCARD_TEL MODIFY TYPE VARCHAR(255);
ALTER TABLE RDAP_VCARD_TEL MODIFY GLOBAL_NUMBER VARCHAR(255);
ALTER TABLE RDAP_VCARD_TEL MODIFY EXT_NUMBER VARCHAR(255);
-- ALTER TABLE RDAP_VCARD_TEL MODIFY PREF VARCHAR(255);

ALTER TABLE REL_ENTITY_REGISTRATION MODIFY ENTITY_ROLE VARCHAR(255);

ALTER TABLE RDAP_EVENT MODIFY EVENT_DATE DATETIME;

ALTER TABLE RDAP_AUTNUM MODIFY NAME VARCHAR(255);
ALTER TABLE RDAP_AUTNUM MODIFY TYPE VARCHAR(255);
ALTER TABLE RDAP_AUTNUM MODIFY COUNTRY VARCHAR(255);
ALTER TABLE RDAP_DSDATA MODIFY DIGEST VARCHAR(2048);
ALTER TABLE REL_SECUREDNS_DSKEY MODIFY REL_DSKEY_TYPE VARCHAR(16);
