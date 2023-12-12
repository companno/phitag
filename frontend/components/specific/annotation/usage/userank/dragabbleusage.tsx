import React, { useState } from 'react';
import {
  DndContext,
  closestCenter,
} from '@dnd-kit/core';
import {
  arrayMove,
  SortableContext,
  verticalListSortingStrategy,
  useSortable,
} from '@dnd-kit/sortable';
import SortableUsage from './sortableussage';
import Usage from '../../../../../lib/model/phitagdata/usage/model/Usage';

interface UsageFieldContainerProps {
  usages: {
    key: string;
    usage: Usage;
  }[];

  handleUsagesReordered: Function;
}

const DraggableUsage: React.FC<UsageFieldContainerProps> = ({ usages, handleUsagesReordered }) => {
  const [orderedUsages, setOrderedUsages] = useState(usages);

  const handleDragEnd = (event: any) => {
    if (!event) {
      return;
    }
    const { active, over } = event;
    if (active.id !== over.id) {
      setOrderedUsages((items) => {
        const activeIndex = items.findIndex((item) => item.key === active.id);
        const overIndex = items.findIndex((item) => item.key === over.id);
        const movedItems = arrayMove(items, activeIndex, overIndex);
        const movedItemsString = movedItems.map((item) => item.key).join(',');
        handleUsagesReordered(movedItemsString)
        return movedItems;
      });
    }
  };

  return (
    <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
      <div className="p-3" style={{ width: '100%' }}>
        <SortableContext
          items={orderedUsages.map((item) => item.key)}
          strategy={verticalListSortingStrategy}
        >
          {orderedUsages.map((usage, index) => (
            <SortableUsage key={usage.key} id={usage.key} usage={usage.usage} />
          ))}
        </SortableContext>
      </div>
    </DndContext>
  );
};

export default DraggableUsage;
