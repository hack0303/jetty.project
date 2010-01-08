// ========================================================================
// Copyright (c) 2006-2009 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
// The Eclipse Public License is available at 
// http://www.eclipse.org/legal/epl-v10.html
// The Apache License v2.0 is available at
// http://www.opensource.org/licenses/apache2.0.php
// You may elect to redistribute this code under either of these licenses. 
// ========================================================================

package org.eclipse.jetty.annotations;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.annotation.MultipartConfig;

import org.eclipse.jetty.annotations.AnnotationIntrospector.AbstractIntrospectableAnnotationHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * MultiPartConfigAnnotationHandler
 *
 *
 */
public class MultiPartConfigAnnotationHandler extends AbstractIntrospectableAnnotationHandler
{
    protected WebAppContext _wac;

    public MultiPartConfigAnnotationHandler(WebAppContext context)
    {
        //TODO verify that we ignore class parent hierarchy
        super(false); 
        _wac = context;
    }
    /** 
     * @see org.eclipse.jetty.annotations.AnnotationIntrospector.AbstractIntrospectableAnnotationHandler#doHandle(java.lang.Class)
     */
    public void doHandle(Class clazz)
    {
        if (!Servlet.class.isAssignableFrom(clazz))
            return;
        
        MultipartConfig multi = (MultipartConfig) clazz.getAnnotation(MultipartConfig.class);
        if (multi == null)
            return;
        
        
        //TODO: The MultipartConfigElement needs to be set on the ServletHolder's Registration.
        //How to identify the correct Servlet?  If the Servlet has no WebServlet annotation on it, does it mean that this MultipartConfig
        //annotation applies to all declared instances in web.xml/programmatically?
        //Assuming TRUE for now.
        
        ServletHolder[] holders = _wac.getServletHandler().getServlets();

        if (holders != null)
        {
            for (ServletHolder h : holders)
            {
                if (h.getClassName().equals(clazz.getName()))
                {
                    h.getRegistration().setMultipartConfig(new MultipartConfigElement(multi));
                }
            }
        }
    }

}
