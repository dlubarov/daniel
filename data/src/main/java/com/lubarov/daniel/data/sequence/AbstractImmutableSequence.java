package com.lubarov.daniel.data.sequence;

public abstract class AbstractImmutableSequence<A>
    extends AbstractSequence<A>
    implements ImmutableSequence<A> {
  @Override
  public ImmutableSequence<A> toImmutable() {
    return this;
  }
}
