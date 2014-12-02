/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.java.checks.methods;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.sonar.java.checks.SubscriptionBaseVisitor;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

public abstract class AbstractMethodDetection extends SubscriptionBaseVisitor {

  protected List<MethodInvocationMatcher> methodInvocationMatchers;
  protected AbstractMethodDetection() {
    //Default constructor
  }
  protected AbstractMethodDetection(MethodInvocationMatcher methodInvocationMatcher) {
    this.methodInvocationMatchers = Lists.newArrayList(methodInvocationMatcher);
  }

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    if (hasSemantic()) {
      MethodInvocationTree mit = (MethodInvocationTree) tree;
      for (MethodInvocationMatcher invocationMatcher : methodInvocationMatchers) {
        if (invocationMatcher.matches(mit, getSemanticModel())) {
          onMethodFound(mit);
        }
      }
    }
  }

  protected void onMethodFound(MethodInvocationTree mit) {
    //Do nothing by default
  }

}
