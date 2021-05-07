package org.javaee7.jpa.ordercolumn;

import static org.junit.Assert.assertEquals;

import jakarta.ejb.EJB;
import jakarta.persistence.OrderColumn;

import org.javaee7.jpa.ordercolumn.entity.bidirectionalmappedby.Child;
import org.javaee7.jpa.ordercolumn.entity.bidirectionalmappedby.Parent;
import org.javaee7.jpa.ordercolumn.service.bidirectionalmappedby.OrderColumnTesterService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This tests and demonstrates the {@link OrderColumn} annotation when used together with a
 * bi-directional parent-child mapping, where the child holds a foreign key to the parent.
 * 
 * <p>
 * This is the normal oneToMany mapping using the <code>mappedBy</code> attribute.
 * Even though this is the most straightforward mapping, because of a mis-interpretation
 * in the JPA spec it was believed for years that this mapping did not have to be
 * supported (see https://hibernate.atlassian.net/browse/HHH-5732 among others)
 * 
 * <p>
 * In this mapping the position each child has in the parent's list is stored in the
 * child table and fully managed by JPA. This means that this position index does
 * not explicitly show up in the object model.
 * 
 * <p>
 * Example SQL DDL (h2 syntax)
 * <pre>
 *      create table Parent (
 *          id bigint generated by default as identity,
 *          
 *          primary key (id)
 *      );
 *      
 *      create table Child (
 *           id bigint generated by default as identity,
 *           parent_id bigint,
 *           children_ORDER integer,
 *           
 *           primary key (id),
 *           foreign key (parent_id) references Parent
 *       );
 * </pre>
 * 
 * 
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class OrderColumnBiMappedByTest {

    @Deployment
    private static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackages(true, Parent.class.getPackage())
            .addPackages(true, OrderColumnTesterService.class.getPackage())
            .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private OrderColumnTesterService indexColumnTesterService;

    @Test
    public void saveInOneGo() {

        Parent parent = new Parent();

        Child child1 = new Child();
        child1.setParent(parent);

        Child child2 = new Child();
        child2.setParent(parent);

        parent.getChildren().add(child1);
        parent.getChildren().add(child2);

        parent = indexColumnTesterService.save(parent);

        Parent savedParent = indexColumnTesterService.getParentById(parent.getId());

        assertEquals("2 children added to parent and saved, but after re-loading number of chilren different",
            2, savedParent.getChildren().size());
    }

    /**
     * Saves a parent instance first, then adds two children instances and saves again.
     * 
     * <p>
     * Example sequence of insert/update statements that may be generated to accomplish this:
     * <pre>
     * insert into Parent (id) values (null)
     * insert into Child (id, parent_id) values (null, 1) 
     * insert into Child (id, parent_id) values (null, 1)
     *
     * update Child set children_ORDER = 0 where id = 1
     * update Child set children_ORDER = 1 where id = 2
     * </pre>
     * 
     */
    @Test
    public void saveParentSeparatelyFirst() {

        Parent parent = indexColumnTesterService.save(new Parent());

        Child child1 = new Child();
        child1.setParent(parent);

        Child child2 = new Child();
        child2.setParent(parent);

        parent.getChildren().add(child1);
        parent.getChildren().add(child2);

        parent = indexColumnTesterService.save(parent);

        Parent savedParent = indexColumnTesterService.getParentById(parent.getId());

        assertEquals("2 children added to parent and saved, but after re-loading number of chilren different",
            2, savedParent.getChildren().size());

    }

}