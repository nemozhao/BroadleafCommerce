/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.cms.url.dao;

import org.broadleafcommerce.cms.url.domain.URLHandler;
import org.broadleafcommerce.cms.url.domain.URLHandlerImpl;
import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by ppatel.
 */
@Repository("blURLHandlerDao")
public class URlHandlerDaoImpl implements URLHandlerDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public URLHandler findURLHandlerByURI(String uri) {
        Query query;
        query = em.createNamedQuery("BC_READ_OUTGOING_URL");
        query.setParameter("incomingURL", uri);

        @SuppressWarnings("unchecked")
        List<URLHandler> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public List<URLHandler> findAllURLHandlers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<URLHandler> criteria = builder.createQuery(URLHandler.class);
        Root<URLHandlerImpl> handler = criteria.from(URLHandlerImpl.class);
        criteria.select(handler);
        try {
            return em.createQuery(criteria).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<URLHandler>();
        }
    }
    
    public URLHandler saveURLHandler(URLHandler handler) {
        return em.merge(handler);
    }

}
