/*
 * This file is part of tuOCCI.
 *
 *     tuOCCI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     tuOCCI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with tuOCCI.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This file is part of tuOCCI.
 *
 *     tuOCCI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     tuOCCI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with tuOCCI.  If not, see <http://www.gnu.org/licenses/>.
 */

package generated.occi.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "link")
public class Link
        extends Entity {

    /**
     * The {@link Resource} instance this <code>Link</code> originates from.
     */
  
    @XmlElement(name = "source")
    private Resource source;

    /**
     * The {@link Resource} instance this <code>Link</code> points to.
     */
  
    @XmlElement(name = "target")
    private Resource target;

    /**
     * Creates a new instance of this class, using the given parameters.
     *
     * @param source
     *         The {@link Resource} instance this <code>Link</code>
     *         originates from.
     * @param target
     *         The {@link Resource} instance this <code>Link</code> points
     *         to.
     */
    public Link(Resource source, Resource target) {
        this.source = source;
        this.target = target;
    }

    /**
     * Returns the resource this link originates from.
     *
     * @return The resource this link originates from.
     */
    public Resource getSource() {
        return source;
    }

    /**
     * Modifies the resource this link originates from.
     *
     * @param source
     *         The new value for the resource this link originates from.
     */
    public void setSource(Resource source) {
        this.source = source;
    }

    /**
     * Returns the resource this link points to.
     *
     * @return The resource this link points to.
     */
    public Resource getTarget() {
        return target;
    }

    /**
     * Modifies the resource this link points to.
     *
     * @param target
     *         The new value for the resource this link points to.
     */
    public void setTarget(Resource target) {
        this.target = target;
    }

    /*
     * TODO: not yet documented
     *
     * @return
     */
   
}
