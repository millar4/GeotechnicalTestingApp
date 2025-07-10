import React, { useState } from 'react';

// PrintableData Component
const PrintableData = ({ details, searchcontent, pattern, formatData }) => {
    const [selectedFields, setSelectedFields] = useState({
        test: false,
        group: false,
        classification: false,
        param: false
    });

    // Toggle selection of each field
    const handleSelection = (field) => {
        setSelectedFields((prevState) => ({
            ...prevState,
            [field]: !prevState[field]
        }));
    };

    // Prepare the printable content based on selection
    const generatePrintableContent = () => {
        const content = [];
        if (selectedFields.test && formatData(details.test, searchcontent, pattern, "test")) {
            content.push(
                <h3 key="test">
                    {formatData(details.test, searchcontent, pattern, "test")}{" "}
                    {details.symbol && (
                        <span>({formatData(details.symbol, searchcontent, pattern, "symbol")})</span>
                    )}
                </h3>
            );
        }
        if (selectedFields.group && formatData(details.group, searchcontent, pattern, "group")) {
            content.push(
                <p key="group"><strong>Group:</strong> {formatData(details.group, searchcontent, pattern, "group")}</p>
            );
        }
        if (selectedFields.classification && formatData(details.classification, searchcontent, pattern, "classification")) {
            content.push(
                <p key="classification"><strong>AGS:</strong> {formatData(details.classification, searchcontent, pattern, "classification")}</p>
            );
        }
        if (selectedFields.param && formatData(details.param, searchcontent, pattern, "param")) {
            content.push(
                <p key="param"><strong>Param:</strong> {formatData(details.param, searchcontent, pattern, "param")}</p>
            );
        }

        return content;
    };

    // Function to print the content
    const handlePrint = () => {
        const content = generatePrintableContent();
        const printWindow = window.open("", "", "width=800,height=600");
        printWindow.document.write("<html><head><title>Print</title>");
        // Optionally add styles for print layout
        printWindow.document.write("<style>body { font-family: Arial, sans-serif; }</style>");
        printWindow.document.write("</head><body>");
        content.forEach(item => {
            // Write each content item to the print window as HTML
            printWindow.document.write(item.outerHTML);
        });
        printWindow.document.write("</body></html>");
        printWindow.document.close();
        printWindow.print();
    };

    return (
        <div>
            {/* Display checkboxes for selecting data */}
            <div>
                <label>
                    <input type="checkbox" checked={selectedFields.test} onChange={() => handleSelection("test")} />
                    Test Name & Symbol
                </label>
                <label>
                    <input type="checkbox" checked={selectedFields.group} onChange={() => handleSelection("group")} />
                    Group
                </label>
                <label>
                    <input type="checkbox" checked={selectedFields.classification} onChange={() => handleSelection("classification")} />
                    AGS
                </label>
                <label>
                    <input type="checkbox" checked={selectedFields.param} onChange={() => handleSelection("param")} />
                    Param
                </label>
            </div>

            {/* Display selected fields */}
            <div>
                {generatePrintableContent()}
            </div>

            {/* Button to trigger print */}
            <button onClick={handlePrint}>Print Selected Info</button>
        </div>
    );
};

export default PrintableData;
