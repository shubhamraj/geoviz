/*
 * TouchGraph LLC. Apache-Style Software License
 *
 *
 * Copyright (c) 2002 Alexander Shapiro. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by 
 *        TouchGraph LLC (http://www.touchgraph.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "TouchGraph" or "TouchGraph LLC" must not be used to endorse 
 *    or promote products derived from this software without prior written 
 *    permission.  For written permission, please contact 
 *    alex@touchgraph.com
 *
 * 5. Products derived from this software may not be called "TouchGraph",
 *    nor may "TouchGraph" appear in their name, without prior written
 *    permission of alex@touchgraph.com.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL TOUCHGRAPH OR ITS CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 *
 */

package geovista.touchgraph.graphelements;

import java.util.Collection;

import geovista.touchgraph.Edge;
import geovista.touchgraph.Node;

/** ImmutableGraphEltSet provides access to the elements of GraphElementSet 
  * that does not allow for addition or deletion of nodes or edges.
  *
  * @author   Alexander Shapiro                                        
  * 
  */
public interface ImmutableGraphEltSet {

    /** Return the current Node count. */
    public int nodeNum();

    /** Return the current Edge count. */
    public int edgeNum();

    /** Return the Node whose ID matches the String <tt>id</tt>, null if no match is found. */
    public Node findNode( String id );

    /** Return a Collection of all Nodes whose label matches the String <tt>label</tt>, 
      * null if no match is found. */
    public Collection findNodesByLabel( String label );

    /** Return an Edge spanning Node <tt>from</tt> to Node <tt>to</tt>. */
    public Edge findEdge( Node from, Node to );

    /** Returns a random node, or null if none exist (for making random graphs). */
    public Node getRandomNode();

    /** Return the first Node, null if none exist. */
    public Node getFirstNode();

    /** iterates through all the nodes. */
    public void forAllNodes( TGForEachNode fen );

    /** iterates through pairs of Nodes. */
    public void forAllNodePairs( TGForEachNodePair fenp );

    /** iterates through Edges. */
    public void forAllEdges( TGForEachEdge fee );

} // end com.touchgraph.graphlayout.graphelements.ImmutableGraphEltSet
