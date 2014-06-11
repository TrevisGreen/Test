/*
 * The MIT License
 *
 * Copyright 2014 Trevis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.trevisgreen.test.web;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trevisgreen.test.model.Connection;
import org.trevisgreen.test.service.UserService;
import org.trevisgreen.test.utils.Constants;

/**
 *
 * @author Trevis
 */
@Controller
public class HomeController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String home(HttpSession session) {
        log.debug("Showing home page");
        if (session.getAttribute("imageUrl") == null && session.getAttribute("noImageUrl") == null) {
            log.debug("Looking for authenticated user");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && !auth.getName().equals("anonymousUser")) {
                log.debug("Looking for social connection for {}", auth.getName());
                Connection connection = userService.getConnection(auth.getName());
                if (connection != null) {
                    session.setAttribute("imageUrl", connection.getImageUrl());
                } else {
                    session.setAttribute("noImageUrl", Boolean.TRUE);
                }
            }
        }

        return "home/home";
    }
    
    @RequestMapping(value = "/currentBackground", params = {"backgroundId"}, method = RequestMethod.POST)
    @ResponseBody
    public String setBackground(HttpSession session, @RequestParam Integer backgroundId) {
        session.setAttribute(Constants.BACKGROUND_ID, backgroundId);
        return "OK";
    }

}
