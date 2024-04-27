import { useState } from "react";

const ClickableButton = ({ children, onSelect, onUnSelect }) => {
  const [selected, setSelect] = useState(false);

  return (
    <button
      className={`btn ${selected ? "" : "btn-outline"} btn-neutral btn-md text-lg`}
      onClick={() => {
        setSelect(!selected);
        selected ? onUnSelect() : onSelect();
      }}
    >
      {selected ? "✓ " : "+ "}
      {children}
    </button>
  );
};

const MultiSelect = ({ list, setSelectedList, selectedList }) => {
  const multiSelectList = Array.from(new Set(list));
  // const [selectedList, setSelectedList] = useState([]);

  // console.log(selectedList)

  const addSelection = (id) => {
    setSelectedList(
      selectedList instanceof Array
        ? [...selectedList, multiSelectList[id]]
        : [multiSelectList[id]],
    );
  };

  const removeSelection = (id) => {
    const elementToRemove = multiSelectList[id];
    setSelectedList(selectedList.filter((elem) => elem !== elementToRemove));
  };

  return (
    <>
      <div className="flex flex-wrap w-full gap-x-2 gap-y-3 justify-center">
        {multiSelectList.map((elem, id) => (
          <ClickableButton
            key={id}
            onSelect={() => addSelection(id)}
            onUnSelect={() => removeSelection(id)}
          >
            {elem}
          </ClickableButton>
        ))}
      </div>
    </>
  );
};

export default MultiSelect;
