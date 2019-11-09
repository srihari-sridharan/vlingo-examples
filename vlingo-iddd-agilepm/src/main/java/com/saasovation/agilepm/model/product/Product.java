// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.model.product;

import com.saasovation.agilepm.model.Tenant;
import io.vlingo.actors.Stage;
import io.vlingo.common.Tuple2;

public interface Product {

    static Tuple2<ProductId, Product> defineWith(
            final Stage stage,
            final Tenant tenant,
            final ProductOwner productOwner,
            final String name,
            final String description,
            final boolean hasDiscussion) {

        assert (tenant != null && tenant.id != null);
        assert (productOwner != null && productOwner.id != null);
        assert (name != null);
        assert (description != null);

        final ProductId productId = ProductId.unique();
        final Product product = stage.actorFor(Product.class, ProductEntity.class, tenant, productId);
        product.define(productOwner, name, description, hasDiscussion);
        return Tuple2.from(productId, product);
    }

    void assignProductOwner(final ProductOwner productOwner);

    void attachDiscussion(final String discussionId);

    void changeDescription(final String description);

    void changeName(final String name);

    void define(final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion);

    void requestDiscussion();

    public static class State {
        public final String description;
        public final String discussionId;
        public final boolean hasDiscussion;
        public final String name;
        public final ProductId productId;
        public final ProductOwner productOwner;
        public final Tenant tenant;

        static State inital(final Tenant tenant, final ProductId productId) {
            return new State(tenant, productId, null, null, null, false, null);
        }

        static State with(final Tenant tenant, final ProductId productId, final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion) {
            return new State(tenant, productId, productOwner, name, description, hasDiscussion);
        }

        State(final Tenant tenant, final ProductId productId, final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion, final String discussionId) {
            this.tenant = tenant;
            this.productId = productId;
            this.productOwner = productOwner;
            this.name = name;
            this.description = description;
            this.hasDiscussion = hasDiscussion;
            this.discussionId = discussionId;
        }

        State(final Tenant tenant, final ProductId productId, final ProductOwner productOwner, final String name, final String description, final boolean hasDiscussion) {
            this(tenant, productId, productOwner, name, description, hasDiscussion, null);
        }

        State withDescription(final String description) {
            return new State(tenant, productId, productOwner, name, description, hasDiscussion, discussionId);
        }

        State withDiscussion(final String discussionId) {
            return new State(tenant, productId, productOwner, name, description, hasDiscussion, discussionId);
        }

        State withDiscussionRequested() {
            return new State(tenant, productId, productOwner, name, description, true, null);
        }

        State withName(final String name) {
            return new State(tenant, productId, productOwner, name, description, hasDiscussion, discussionId);
        }

        State withProductOwner(final ProductOwner productOwner) {
            return new State(tenant, productId, productOwner, name, description, hasDiscussion, discussionId);
        }
    }
}
