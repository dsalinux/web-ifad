/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.

Oracle and Java are registered trademarks of Oracle and/or its affiliates.
Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.util.HibernateUtil;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 * @author Danilo Souza
 */
public abstract class AbstractManager extends GenericBean {

    private static final Logger LOGGER = Logger.getLogger(AbstractManager.class.getSimpleName());
    
    protected final <T> T doInTransaction(PersistenceAction<T> action) throws BusinessException {
        Transaction transaction = HibernateUtil.getSession().getTransaction();
        if(transaction == null){
            transaction = HibernateUtil.getSession().beginTransaction();
        } else if(!transaction.isActive()){
            transaction.begin();
        }
        try {
            T result = action.execute(HibernateUtil.getSession());
            transaction.commit();
            return result;
        } catch(BusinessException e) {
            try {
                transaction.rollback();
                HibernateUtil.getSession().close();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Problemanas na tentativa de realizar rollback. ", ex);
            }
            throw e;
        } catch (Exception e) {
            try {
                LOGGER.log(Level.SEVERE, "Ocorreu um erro no sistema!", e);
                transaction.rollback();
                HibernateUtil.getSession().close();
                throw new BusinessException("Ocorreu um erro greve no sistema!", e);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Problemanas na tentativa de realizar rollback. ", ex);
            }
            throw new RuntimeException(e);
        } finally {
//            HibernateUtil.getSession().flush();
//            HibernateUtil.getSession().clear();
//            HibernateUtil.getSession().close();
        }

    }

    protected final void doInTransaction(PersistenceActionWithoutResult action) throws BusinessException {
        Transaction transaction = HibernateUtil.getSession().getTransaction();
        if(transaction == null){
            transaction = HibernateUtil.getSession().beginTransaction();
        } else if(!transaction.isActive()){
            transaction.begin();
        }
        try {
            action.execute(HibernateUtil.getSession());
            transaction.commit();
        } catch(BusinessException e) {
            try {
                transaction.rollback();
                HibernateUtil.getSession().close();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Problemanas na tentativa de realizar rollback. ", ex);
            }
            throw e;
        } catch (Exception e) {
            try {
                LOGGER.log(Level.SEVERE, "Ocorreu um erro no sistema!", e);
                transaction.rollback();
                HibernateUtil.getSession().close();
                throw new BusinessException("Ocorreu um erro greve no sistema!", e);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Problemanas na tentativa de realizar rollback. ", ex);
            }
            throw new RuntimeException(e);
        } finally {
//            HibernateUtil.getSession().flush();
//            HibernateUtil.getSession().clear();
//            HibernateUtil.getSession().close();
        }
    }

    protected static interface PersistenceAction<T> {

        T execute(Session s) throws BusinessException ;
    }

    protected static interface PersistenceActionWithoutResult {

        void execute(Session s)throws BusinessException;
    }

    protected Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class for logger is required.");
        }
        return Logger.getLogger(clazz.getName());
    }

    protected void publishEvent(Class<? extends SystemEvent> eventClass, Object source) {
        if (source != null) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getApplication().publishEvent(ctx, eventClass, source);
        }
    }

    protected void subscribeToEvent(Class<? extends SystemEvent> eventClass, SystemEventListener listener) {
        FacesContext.getCurrentInstance().getApplication().subscribeToEvent(eventClass, listener);
    }

    protected void unsubscribeFromEvent(Class<? extends SystemEvent> eventClass, SystemEventListener listener) {
        FacesContext.getCurrentInstance().getApplication().unsubscribeFromEvent(eventClass, listener);
    }
    
    public Session getSession(){
        return HibernateUtil.getSession();
    }
}
