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
package cn.cnnic.rdap.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.cnnic.rdap.BaseTest;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerEntitySearchTest extends BaseTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    /**
     * output json.
     */
    final private String rdapJson = "application/json";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test query exist.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/entity-search.xml")
    public void testQueryExist() throws Exception {
        String entityHandle = "truncated*";
        mockMvc.perform(
                get("/.well-known/rdap/entities?handle=" + entityHandle)
                        .accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.entitySearchResults").exists())
                .andExpect(jsonPath("$.entitySearchResults").isArray())
                .andExpect(
                        jsonPath("$.entitySearchResults",
                                Matchers.hasItem(Matchers.hasKey("handle"))))
                .andExpect(
                        jsonPath("$.entitySearchResults", Matchers
                                .hasItem(Matchers.hasValue("truncated1"))))
                .andExpect(
                        jsonPath("$.entitySearchResults", Matchers
                                .hasItem(Matchers.hasValue("truncated2"))))
                .andExpect(
                        jsonPath("$.entitySearchResults[0].vcardArray")
                                .exists())
                .andExpect(
                        jsonPath("$.entitySearchResults[0].vcardArray[1]")
                                .exists())
                .andExpect(
                        jsonPath("$.entitySearchResults[0].vcardArray[1]")
                                .isArray())
                .andExpect(
                        jsonPath("$.entitySearchResults[0].vcardArray[1][1]")
                                .exists())
                .andExpect(
                        jsonPath("$.entitySearchResults[0].vcardArray[1][1][3]")
                                .value("individual"))
                .andExpect(
                        jsonPath("$.entitySearchResults[0].port43").value(
                                "whois.example.net"));

    }

    /**
     * test query non exist.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/entity-search.xml")
    public void testQueryNonExistAutnum() throws Exception {
        String nonExistHandle = "1000000";
        mockMvc.perform(
                get("/.well-known/rdap/entities?handle=" + nonExistHandle)
                        .accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404));
    }

    /**
     * test query with invalid handle.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/entity-search.xml")
    public void testQueryAutnumWithInvalidQ() throws Exception {
        String invalidHandle = "";
        mockMvc.perform(
                get("/.well-known/rdap/entities?handle=" + invalidHandle)
                        .accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400));
    }

}
