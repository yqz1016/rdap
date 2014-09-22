/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.restfulwhois.rdap.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.bean.Autnum;
import org.restfulwhois.rdap.bean.BaseModel;
import org.restfulwhois.rdap.bean.Domain;
import org.restfulwhois.rdap.bean.Entity;
import org.restfulwhois.rdap.bean.Nameserver;
import org.restfulwhois.rdap.bean.Network;
import org.restfulwhois.rdap.bean.Principal;
import org.restfulwhois.rdap.bean.SecureObject;
import org.restfulwhois.rdap.common.RdapProperties;
import org.restfulwhois.rdap.controller.support.PrincipalHolder;
import org.restfulwhois.rdap.dao.AclDao;
import org.restfulwhois.rdap.service.AccessControlManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;



/**
 * AccessControlManager implementation.
 * 
 * Requirement from  
 * http://tools.ietf.org/html/draft-ietf-weirds-rdap-sec-06#section-3.2 .
 * 
 * Provide authorization to user and its access object, 
 * according to Access Control List.
 * 
 * @author jiashuo
 * 
 */
@Service
public class AccessControlManagerImpl implements AccessControlManager {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AccessControlManagerImpl.class);
    /**
     * all_secure_onbject_type.
     */
    private static final List<Class<?>> ALL_SECURE_OBJECT_TYPE = 
             new ArrayList<Class<?>>();
    
    static {        
        ALL_SECURE_OBJECT_TYPE.add(Domain.class);
        ALL_SECURE_OBJECT_TYPE.add(Entity.class);
        ALL_SECURE_OBJECT_TYPE.add(Autnum.class);
        ALL_SECURE_OBJECT_TYPE.add(Network.class);
        ALL_SECURE_OBJECT_TYPE.add(Nameserver.class);
    }
    
    /**
     * acl dao.
     */
    @Autowired
    private AclDao aclDao;
    
    /**
     * Is a model permitted for querying.
     * 
     * @param object
     *            param for object to be queried.
     * @return boolean.
     */   
    @Override
    public boolean hasPermission(final BaseModel object) {
        Assert.notNull(object);
        Assert.notNull(object.getId());
        Assert.notNull(object.getObjectType());
        
        LOGGER.debug("hasPermission:" + object);
        Principal principal = PrincipalHolder.getPrincipal();
        SecureObject secureObject = new SecureObject(object.getId(), object
                .getObjectType().getName());
        return aclDao.hasEntry(principal, secureObject);
    }
    
    /**
     * Is a model permitted for querying innerObject.
     * 
     * @param object
     *            param for object to be queried.
     * 
     */ 

    @Override
    public void innerObjectHasPermission(BaseModel object) {
        String needCheckAccess = RdapProperties.getNeedCheckAccess();
        if (object == null || StringUtils.isBlank(needCheckAccess) 
            || needCheckAccess.equals("false")) {
             return;
        }        
        Field[] allField = object.getClass().getDeclaredFields();
        if (allField == null) {
            return;
        }
        for (int i = 0; i < allField.length; i++) {
          if (allField[i].getType().isPrimitive()) {
             continue;
           }
          if (allField[i].getType().getName().startsWith("java.lang")) {
              continue;
          }
          try {
                allField[i].setAccessible(true);
                Object value = allField[i].get(object);
                if (value == null) {
                    continue;
                }
                if (value != null && value instanceof List) {
                    if (((List<?>) value).size() > 0
                        && isSecureObjectType(((List<?>) value).get(0))) {
                        for (int j = 0; j < ((List<?>) value).size(); j++) {
                             Object valueIn = ((List<?>) value).get(j);
                             if (hasPermission((BaseModel) valueIn)) {
                                  innerObjectHasPermission((BaseModel) valueIn);
                             } else {
                                  ((List<?>) value).remove(j);
                             }
                         }
                         allField[i].set(object, value);
                     }
                 } else if (isSecureObjectType(value)) {
                      if (hasPermission((BaseModel) value)) {
                          innerObjectHasPermission((BaseModel) value);
                      } else {
                          allField[i].set(object, null);
                      }
                 }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    /**
     * check if object is main Object .
     * 
     * @param object
     *            object.
     * @return true if is,false if not.
     */
     private boolean isSecureObjectType(Object object) {

        if (ALL_SECURE_OBJECT_TYPE.contains(object.getClass())) {
           return true;
        }
        return false;
     }
}
