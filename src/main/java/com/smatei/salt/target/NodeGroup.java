package com.smatei.salt.target;

/**
 * Nodegroup target: {@link https://docs.saltstack.com/en/latest/topics/targeting/nodegroups.htm}
 *
 * @author smatei
 *
 */
public class NodeGroup extends AbstractTarget<String> implements ITarget<String>
{
  /**
   * Constructor expecting a nodegroup as string.
   *
   * @param nodegroup
   *          the nodegroup as string
   */
  public NodeGroup(String nodegroup)
  {
    super(TargetType.NODEGROUP, nodegroup);
  }
}
