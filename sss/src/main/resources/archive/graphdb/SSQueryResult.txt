/**
* Code contributed to the Learning Layers project
* http://www.learning-layers.eu
* Development is partly funded by the FP7 Programme of the European Commission under
* Grant Agreement FP7-ICT-318209.
* Copyright (c) 2014, Graz University of Technology - KTI (Knowledge Technologies Institute).
* For a list of contributors see the AUTHORS file at the top-level directory of this distribution.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
 package at.kc.tugraz.ss.serv.db.datatypes.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SSQueryResult implements List<SSQueryResultItem> {

  private List<SSQueryResultItem> set = null;

  public SSQueryResult() {
    this.set = new ArrayList<SSQueryResultItem>();
  }

  public boolean add(SSQueryResultItem arg0) {
    return set.add(arg0);
  }

  public void add(int arg0, SSQueryResultItem arg1) {
    set.add(arg0, arg1);
  }

  public boolean addAll(Collection<? extends SSQueryResultItem> arg0) {
    return set.addAll(arg0);
  }

  public boolean addAll(int arg0, Collection<? extends SSQueryResultItem> arg1) {
    return set.addAll(arg0, arg1);
  }

  public void clear() {
    set.clear();
  }

  public boolean contains(Object arg0) {
    return set.contains(arg0);
  }

  public boolean containsAll(Collection<?> arg0) {
    return set.containsAll(arg0);
  }

  public SSQueryResultItem get(int arg0) {
    return set.get(arg0);
  }

  public int indexOf(Object arg0) {
    return set.indexOf(arg0);
  }

  public boolean isEmpty() {
    return set.isEmpty();
  }

  public Iterator<SSQueryResultItem> iterator() {
    return set.iterator();
  }

  public int lastIndexOf(Object arg0) {
    return set.lastIndexOf(arg0);
  }

  public ListIterator<SSQueryResultItem> listIterator() {
    return set.listIterator();
  }

  public ListIterator<SSQueryResultItem> listIterator(int arg0) {
    return set.listIterator(arg0);
  }

  public boolean remove(Object arg0) {
    return set.remove(arg0);
  }

  public SSQueryResultItem remove(int arg0) {
    return set.remove(arg0);
  }

  public boolean removeAll(Collection<?> arg0) {
    return set.removeAll(arg0);
  }

  public boolean retainAll(Collection<?> arg0) {
    return set.retainAll(arg0);
  }

  public SSQueryResultItem set(int arg0, SSQueryResultItem arg1) {
    return set.set(arg0, arg1);
  }

  public int size() {
    return set.size();
  }

  public List<SSQueryResultItem> subList(int arg0, int arg1) {
    return set.subList(arg0, arg1);
  }

  public Object[] toArray() {
    return set.toArray();
  }

  public <T> T[] toArray(T[] arg0) {
    return set.toArray(arg0);
  }
}