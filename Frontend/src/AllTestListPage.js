import './AllTestListPage.css';
import './index.css';
import React, { useState, useEffect } from 'react';
import Draggable from 'react-draggable';
import { ResizableBox } from 'react-resizable';
import 'react-resizable/css/styles.css';
import { useLocation, useNavigate, Link } from 'react-router-dom';
import lockIcon from './Lock4.png';

// Helper function to escape special characters in search term for regex
const escapeRegExp = (string) => {
    return string.replace(/[.*+?^=!:${}()|\[\]\/\\]/g, '\\$&');
};

// Function to format and highlight the search content
const formatData = (data, searchcontent, pattern, fieldName) => {
    if (typeof data === 'string' && searchcontent) {
        // If pattern is "Quick Search", highlight the search term in the string
        if (pattern === "Quick Search") {
            // Escape the search content to make it regex-safe
            const regex = new RegExp(`(${escapeRegExp(searchcontent)})`, 'gi');
            return (
                <span
                    dangerouslySetInnerHTML={{
                        __html: data.replace(regex, '<span class="highlight">$1</span>')
                    }}
                />
            );
        }

        // Determine the field based on the selected pattern
        let targetField = "";
        switch (pattern) {
            case "Test name":
                targetField = "test";
                break;
            case "Test group":
                targetField = "group";
                break;
            case "Test method":
                targetField = "testMethod";
                break;
            case "Test parameters":
                targetField = "parameters";
                break;
            case "Classification":
                targetField = "classification";
                break;
            default:
                break;
        }

        // If the target field matches the current fieldName, highlight the search term
        if (fieldName === targetField) {
            const regex = new RegExp(`(${escapeRegExp(searchcontent)})`, 'gi');
            return (
                <span
                    dangerouslySetInnerHTML={{
                        __html: data.replace(regex, '<span class="highlight">$1</span>')
                    }}
                />
            );
        }
    }
    return data; // Return data without any changes if no matches
};


// Box component: displays a brief summary of a test item.
const Box = ({
    id,
    test,
    group,
    classification,
    symbol,
    parameters,
    testMethod,
    alt1,
    alt2,
    alt3,
    sampleType,
    fieldSampleMass,
    specimenType,
    specimenMass,
    specimenNumbers,
    specimenD,
    specimenL,
    specimenW,
    specimenH,
    specimenMaxGrainSize,
    specimenMaxGrainFraction,
    isActive,
    onClick,
    searchcontent,
    pattern
}) => {
    return (
        <button className={`box ${isActive ? 'active' : ''}`} onClick={onClick}>
            {formatData(test, searchcontent, pattern, "test") && (
                <h3>Test: {formatData(test, searchcontent, pattern, "test")}</h3>
            )}
            {formatData(group, searchcontent, pattern, "group") && (
                <p>Group: {formatData(group, searchcontent, pattern, "group")}</p>
            )}
             {formatData(classification, searchcontent, pattern, "classification") && (
                <p>AGS: {formatData(classification, searchcontent, pattern, "classification")}</p>
            )}
            {formatData(symbol, searchcontent, pattern, "symbol") && (
                <p>Symbol: {formatData(symbol, searchcontent, pattern, "symbol")}</p>
            )}
            {formatData(parameters, searchcontent, pattern, "parameters") && (
                <p>Parameters: {formatData(parameters, searchcontent, pattern, "parameters")}</p>
            )}
            {formatData(testMethod, searchcontent, pattern, "testMethod") && (
                <p>Test Method: {formatData(testMethod, searchcontent, pattern, "testMethod")}</p>
            )}
        </button>
    );
};

// Helper function to get auth headers before each request
const getAuthHeaders = () => {
    const currentToken = localStorage.getItem('token');
    const headers = {
        "Content-Type": "application/json",
        "Accept": "application/json"
    };
    if (currentToken) headers["Authorization"] = `Bearer ${currentToken}`;
    return headers;
};

// FloatingDetails component: displays detailed info in a draggable/resizable window.
const FloatingDetails = ({ details, onClose, position, searchcontent, pattern }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(() =>
      JSON.parse(localStorage.getItem('isLoggedIn')) || false
    );
  
    const [showPasswordPrompt, setShowPasswordPrompt] = useState(false);
    const [password, setPassword] = useState('');

    const navigate = useNavigate(); 
  
    useEffect(() => {
      const handleStorageChange = () => {
        setIsLoggedIn(JSON.parse(localStorage.getItem('isLoggedIn')) || false);
      };
      window.addEventListener('storage', handleStorageChange);
      return () => window.removeEventListener('storage', handleStorageChange);
    }, []);
  
    const formatData = (data) => data;
  
    const handleEditClick = () => {
        const role = localStorage.getItem('role');
        if (role !== 'ADMIN') {
          alert('You need ADMIN privileges to edit this item.');
          return;
        }
        const databaseType = localStorage.getItem('databaseType') || 'soil';
        navigate('/edititem', {
        state: {
            details,
            databaseType,
        }
        });
      };
    return (
        <Draggable handle=".floating-header">
            <ResizableBox
                width={400}
                height={400}
                minConstraints={[300, 300]}
                maxConstraints={[800, 600]}
                className="floating-details"
                style={{ position: 'fixed', top: position.y, left: position.x }}
            >
                <button className="close-button" onClick={() => onClose(details.id)}>×</button>
                <div className="floating-content">
                    <div className="floating-header">
                        <h3>Detailed Information</h3>
                        {isLoggedIn && (
                            <button className="edit-button" onClick={handleEditClick}>Edit</button>
                    )}
                    </div>
                    {formatData(details.test, searchcontent, pattern, "test") && (
                        <p><strong>Test:</strong> {formatData(details.test, searchcontent, pattern, "test")}</p>
                    )} 
                    {formatData(details.group, searchcontent, pattern, "group") && (
                        <p><strong>Group:</strong> {formatData(details.group, searchcontent, pattern, "group")}</p>
                    )}
                    {formatData(details.classification, searchcontent, pattern, "classification") && (
                        <p><strong>AGS:</strong> {formatData(details.classification, searchcontent, pattern, "classification")}</p>
                    )}
                    {formatData(details.symbol, searchcontent, pattern, "symbol") && (
                        <p><strong>Symbol:</strong> {formatData(details.symbol, searchcontent, pattern, "symbol")}</p>
                    )}
                    {formatData(details.parameters, searchcontent, pattern, "parameters") && (
                        <p><strong>Parameters:</strong> {formatData(details.parameters, searchcontent, pattern, "parameters")}</p>
                    )}
                    {formatData(details.testMethod, searchcontent, pattern, "testMethod") && (
                        <p><strong>Test Method:</strong> {formatData(details.method, searchcontent, pattern, "testMethod")}</p>
                    )}
                    <h4>Additional Fields</h4>
                    {formatData(details.alt1) && (
                        <p><strong>Alt1:</strong> {formatData(details.alt1)}</p>
                    )}
                    {formatData(details.alt2) && (
                        <p><strong>Alt2:</strong> {formatData(details.alt2)}</p>
                    )}
                    {formatData(details.alt3) && (
                        <p><strong>Alt3:</strong> {formatData(details.alt3)}</p>
                    )}
                    {formatData(details.sampleType) && (
                        <p><strong>Sample Type:</strong> {formatData(details.sampleType)}</p>
                    )}
                    {formatData(details.fieldSampleMass) && (
                        <p><strong>Field Sample Mass(kg):</strong> {formatData(details.fieldSampleMass)}</p>
                    )}
                    {formatData(details.specimenType) && (
                        <p><strong>Specimen Type:</strong> {formatData(details.specimenType)}</p>
                    )}
                    {formatData(details.specimenMass) && (
                        <p><strong>Specimen Mass(kg):</strong> {formatData(details.specimenMass)}</p>
                    )}
                    {formatData(details.specimenNumbers) && (
                        <p><strong>Specimen Numbers:</strong> {formatData(details.specimenNumbers)}</p>
                    )}
                    {formatData(details.specimenD) && (
                        <p><strong>Specimen D(mm):</strong> {formatData(details.specimenD)}</p>
                    )}
                    {formatData(details.specimenL) && (
                        <p><strong>Specimen L(mm):</strong> {formatData(details.specimenL)}</p>
                    )}
                    {formatData(details.specimenW) && (
                        <p><strong>Specimen W(mm):</strong> {formatData(details.specimenW)}</p>
                    )}
                    {formatData(details.specimenH) && (
                        <p><strong>Specimen H(mm):</strong> {formatData(details.specimenH)}</p>
                    )}
                    {formatData(details.specimenMaxGrainSize) && (
                        <p><strong>Specimen Max Grain Size(mm):</strong> {formatData(details.specimenMaxGrainSize)}</p>
                    )}
                    {formatData(details.specimenMaxGrainFraction) && (
                        <p><strong>Specimen Max Grain Fraction(d/D):</strong> {formatData(details.specimenMaxGrainFraction)}</p>
                    )}
                    {formatData(details.schedulingNotes) && (
                        <p><strong>Scheduling Notes:</strong> {formatData(details.schedulingNotes)}</p>
                    )}
                    {formatData(details.materials) && (
                        <p><strong>Materials:</strong> {formatData(details.materials)}</p>
                    )}
                    {formatData(details.applications) && (
                        <p><strong>Applications:</strong> {formatData(details.applications)}</p>
                    )}
                </div>
            </ResizableBox>
        </Draggable>
    
    );
};

const PaginatedBoxes = () => {
    const navigate = useNavigate();
    const location = useLocation();

    // Token and login status (token security logic remains unchanged)
    const [token, setToken] = useState(localStorage.getItem('token'));
    const isLoggedIn = !!token;


    // State initialization
    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);
    const [sortOrder, setSortOrder] = useState('default');
    const [selectedBoxes, setSelectedBoxes] = useState([]);

    // Group filter state
    const [groups, setGroups] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState('');

    // Jump page number state
    const [jumpPage, setJumpPage] = useState('');

    // Animation state for canvas nest control
    const [animationEnabled, setAnimationEnabled] = useState(true);

    // Extract initial search parameters and database type from route state
    const { pattern: initialPattern, searchcontent: initialSearchContent, databaseType: passedDB } = location.state || { pattern: '', searchcontent: '', databaseType: 'soil' };

    // Maintain last search content and pattern for toggling between views
    const [lastSearchContent, setLastSearchContent] = useState(initialSearchContent);
    const [lastPattern, setLastPattern] = useState(initialPattern);

    // viewMode: true displays search results, false displays complete test list
    const [viewMode, setViewMode] = useState(!!initialSearchContent);

    // Database type state
    const [databaseType, setDatabaseType] = useState(passedDB || 'soil');

    // State to control search sorting
    const [searchSort, setSearchSort] = useState(false);

    // Effective search content
    const [effectiveSearchContent, setEffectiveSearchContent] = useState(initialSearchContent || '');

    // Headers for fetch requests
    const [headers, setHeaders] = useState({
        "Content-Type": "application/json",
        "Accept": "application/json",
        "Authorization": `Bearer ${localStorage.getItem('token')}` // Retrieve the token from localStorage
    });

    // Sorting mode (e.g., 'default' or 'classification')
    const [sortMode, setSortMode] = useState('default');


    const alphanumericCompare = (a, b) => {
        const regex = /([a-zA-Z]+)(\d+)/;
    
        const matchA = a.match(regex);
        const matchB = b.match(regex);
    
        console.log(`Comparing: ${a} vs ${b}`);
    
        if (!matchA || !matchB) return a.localeCompare(b); // safer fallback
    
        const alphaA = matchA[1];
        const numA = parseInt(matchA[2], 10);
    
        const alphaB = matchB[1];
        const numB = parseInt(matchB[2], 10);
    
        if (alphaA < alphaB) return -1;
        if (alphaA > alphaB) return 1;
    
        return numA - numB;
    };
    
    
    const sortDataByClassification = (data, sortOrder = 'ascending') => {
        if (!Array.isArray(data)) {
            console.error("sortDataByClassification: data is not an array", data);
            return [];
        }
    
        const classifiedItems = data.filter(item => item.classification?.trim());
        const unclassifiedItems = data.filter(item => !item.classification?.trim());
    
        const sortedClassified = classifiedItems.sort((a, b) => {
            const aClass = a.classification.trim();
            const bClass = b.classification.trim();
            return sortOrder === 'ascending'
                ? alphanumericCompare(aClass, bClass)
                : alphanumericCompare(bClass, aClass);
        });
    
        // Return sorted classified items first, then unclassified items last
        return [...sortedClassified, ...unclassifiedItems];
    };

    const sortDataByTestMethod = (data, sortOrder = 'ascending') => {
        if (!Array.isArray(data)) {
            console.error("sortDataBytestMethod: data is not an array", data);
            return [];
        }
    
        const classifiedItems = data.filter(item => item.testMethod?.trim());
        const unclassifiedItems = data.filter(item => !item.testMethod?.trim());
    
        const sortedClassified = classifiedItems.sort((a, b) => {
            const aClass = a.testMethod.trim();
            const bClass = b.testMethod.trim();
            return sortOrder === 'ascending'
                ? alphanumericCompare(aClass, bClass)
                : alphanumericCompare(bClass, aClass);
        });
    
        // Return sorted classified items first, then unclassified items last
        return [...sortedClassified, ...unclassifiedItems];
    };
    
    const sortDataByParameter = (data, sortOrder = 'ascending') => {
        if (!Array.isArray(data)) {
            console.error("sortDataBytestMethod: data is not an array", data);
            return [];
        }
    
        const classifiedItems = data.filter(item => item.parameters?.trim());
        const unclassifiedItems = data.filter(item => !item.parameters?.trim());
    
        const sortedClassified = classifiedItems.sort((a, b) => {
            const aClass = a.parameters.trim();
            const bClass = b.parameters.trim();
            return sortOrder === 'ascending'
                ? alphanumericCompare(aClass, bClass)
                : alphanumericCompare(bClass, aClass);
        });
    
        // Return sorted classified items first, then unclassified items last
        return [...sortedClassified, ...unclassifiedItems];
    };
    
        
    // Function to toggle sort order in search mode (ascending/descending)
    const toggleSearchSortOrder = () => {
        setSearchSort(prevSort => !prevSort); // Toggle searchSort state
    };

    // Ensure that you handle the sort order in the page render or data fetching
    useEffect(() => {
        if (viewMode && effectiveSearchContent) {
            // Adjust sorting based on the current search mode
            const sortedData = sortDataByClassification(data, searchSort ? 'ascending' : 'descending');
            setData(sortedData);
        }
    }, [searchSort, viewMode, effectiveSearchContent]);  // Re-run whenever the searchSort or search content changes
    

    // API base URL based on database type
    const baseUrl = (databaseType === "aggregate")
        ? "http://localhost:8080/aggregate"
        : (databaseType === "rocks")
        ? "http://localhost:8080/rocks"
        : (databaseType === "concrete")
        ? "http://localhost:8080/concrete"
        : (databaseType === "inSituTest")
        ? "http://localhost:8080/inSituTest"
        : "http://localhost:8080/database";


    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const [isSmallScreen, setIsSmallScreen] = useState(window.innerWidth < 768);

    const getBaseUrl = (type) => {
        switch (type) {
            case 'soil': return '/soil';
            case 'aggregate': return '/aggregate';
            case 'rocks': return '/rocks';
            case 'concrete': return '/concrete';
            case 'inSituTest': return '/inSituTest';
            default: return '/soil'; // Fallback to prevent undefined
        }
    };
    let url = ""; 
    useEffect(() => {
        const fetchData = async () => {
            if (!token) {
                setData([]);
                return;
            }
    
            setData([]);
            const headers = getAuthHeaders();
            const effectiveSearchContent = viewMode ? lastSearchContent : "";
            const encodedSearch = encodeURIComponent(effectiveSearchContent);
            let url = `${baseUrl}/all`; // Default URL, might change based on search
    
            if (effectiveSearchContent) {
                // You can modify the URL based on the `effectiveSearchContent` if you are performing a search
                url = `${baseUrl}/search?query=${encodedSearch}`;
            }
    
            try {
                const response = await fetch(url, { testMethod: "GET", headers });
                if (!response.ok) throw new Error(`Failed to fetch data. HTTP Status: ${response.status}`);
                
                let result = await response.json();
                // Apply group filtering if needed
                if (selectedGroup) {
                    result = result.filter(item => item.group?.trim().toLowerCase() === selectedGroup.trim().toLowerCase());
                }
    
                setData(result); // Set the sorted data
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };
    
        fetchData();
    }, [viewMode, lastSearchContent, sortOrder, selectedGroup, token]);
    
    
    useEffect(() => {
    const handleResize = () => {
        setIsSmallScreen(window.innerWidth < 768);
        if (window.innerWidth >= 768) {
        setIsSidebarOpen(false);
        }
    };
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
    }, []);

    useEffect(() => {
        const handleStorageChange = (e) => {
          if (e.key === 'token') {
            setToken(e.newValue);
          }
        };
        window.addEventListener('storage', handleStorageChange);
        return () => window.removeEventListener('storage', handleStorageChange);
      }, []);

    // Override local state if database type is passed in
    useEffect(() => {
        if (passedDB) {
            setDatabaseType(passedDB);
        }
    }, [passedDB]);

    // (Removed the effect that hid the background canvas so that the canvas remains visible)

    useEffect(() => {
        const { searchcontent, pattern } = location.state || { searchcontent: '', pattern: '' };
        setLastSearchContent(searchcontent);
        setLastPattern(pattern);
        setViewMode(!!searchcontent);
      }, [location.state]);

    // Refresh token on window focus (token security logic remains unchanged)
    useEffect(() => {
        const onFocus = () => {
            const newToken = localStorage.getItem('token');
            if (newToken !== token) {
                setToken(newToken);
            }
        };
        window.addEventListener('focus', onFocus);
        return () => window.removeEventListener('focus', onFocus);
    }, [token]);

    // Fetch group data
    const fetchGroups = async () => {
        try {
            const response = await fetch(`${baseUrl}/groups`, { testMethod: "GET", headers: getAuthHeaders() });
            const result = await response.json();
            console.log("Fetched groups:", result);
            
            if (Array.isArray(result)) {
                setGroups(result);
            } else if (result.groups) {
                setGroups(result.groups);
            } else if (Array.isArray(result.data)) {
                setGroups(result.data.map(g => g.group));
            } else {
                console.error("Unexpected response format:", result);
                setGroups([]);
            }
        } catch (error) {
            console.error("Error fetching groups:", error);
            setGroups([]);
        }
    };
    
    useEffect(() => {
        fetchGroups();
    }, [baseUrl, token]);
    
    const getSortedData = (data, sortOrder) => {
        if (!Array.isArray(data)) {
            console.error("Expected array data but got:", data);
            return []; // Return empty array if data is not an array
        }

    function debounce(fn, delay) {
        let timeout;
        return function(...args) {
            clearTimeout(timeout);
            timeout = setTimeout(() => fn(...args), delay);
        };
    }
    
        const sortFunctions = {
            default: (a, b) => (a.id ?? 0) - (b.id ?? 0),
            
            test: (a, b) => {
                const aVal = a.test?.trim();
                const bVal = b.test?.trim();
        
                const aHas = !!aVal;
                const bHas = !!bVal;
        
                if (aHas && bHas) return aVal.localeCompare(bVal);
                if (aHas) return -1;
                if (bHas) return 1;
                return 0;
            },
        
            name: (a, b) => (a.group ?? '').localeCompare(b.group ?? ''),
            testMethod: (a, b) => {
                const aClass = a.testMethod?.trim();
                const bClass = b.testMethod?.trim();
        
                const aHasClass = !!aClass;
                const bHasClass = !!bClass;
        
                if (aHasClass && bHasClass) {
                    return alphanumericCompare(aClass, bClass);
                }
        
                if (aHasClass) return -1;
                if (bHasClass) return 1;
        
                return 0;
            },          
        
            parameters: (a, b) => {
                const aClass = a.testMethod?.trim();
                const bClass = b.testMethod?.trim();
        
                const aHasClass = !!aClass;
                const bHasClass = !!bClass;
        
                if (aHasClass && bHasClass) {
                    return alphanumericCompare(aClass, bClass);
                }
        
                if (aHasClass) return -1;
                if (bHasClass) return 1;
        
                return 0;
            },
        
            classification: (a, b) => {
                const aClass = a.classification?.trim();
                const bClass = b.classification?.trim();
        
                const aHasClass = !!aClass;
                const bHasClass = !!bClass;
        
                if (aHasClass && bHasClass) {
                    return alphanumericCompare(aClass, bClass);
                }
        
                if (aHasClass) return -1;
                if (bHasClass) return 1;
        
                return 0;
            },
        };
            
    
        const sortFunction = sortFunctions[sortOrder] || sortFunctions.default;
        return data.sort(sortFunction);
    };
    
    const fetchData = async () => {
        if (!token) {
            setData([]);
            return;
        }
    
        console.log("fetchData() triggered");
        setData([]);
        const headers = getAuthHeaders();
        const effectiveSearchContent = viewMode ? lastSearchContent : "";
        const effectivePattern = viewMode ? lastPattern : "";
        const encodedSearch = encodeURIComponent(effectiveSearchContent);
        let urls = [];
        let url = "";
    
        if (!effectiveSearchContent) {
            if (selectedGroup) {
                url = `${baseUrl}/group?group=${encodeURIComponent(selectedGroup)}`;
                console.log(`Group filter active. GET ${url}`);
                try {
                    const response = await fetch(url, { testMethod: "GET", headers });
                    if (!response.ok) throw new Error(`Failed to fetch group data. HTTP Status: ${response.status}`);
                    const result = await response.json();
                    const filteredResult = result.filter(item => item.group?.trim().toLowerCase() === selectedGroup.trim().toLowerCase());
                    setData(filteredResult);
                    return;
                } catch (error) {
                    console.error("Error fetching group data:", error);
                    return;
                }
            }
    
            url = `${baseUrl}/all`;
            if (sortOrder !== "default") {
                url += (url.includes("?") ? "&" : "?") + `sort=${encodeURIComponent(sortOrder)}`;
            }
    
            console.log(`Fetching data from: ${url}`);
            try {
                const response = await fetch(url, { testMethod: "GET", headers });
                if (!response.ok) throw new Error(`Failed to fetch data. HTTP Status: ${response.status}`);
                
                let result = await response.json();

                if (selectedGroup) {
                    result = result.filter(item => item.group?.trim().toLowerCase() === selectedGroup.trim().toLowerCase());
                }
            
                if (sortOrder === 'classification') {
                    result = sortDataByClassification(result, 'ascending');
                } else if (sortOrder === 'testMethod')
                    result = sortDataByTestMethod(result, 'ascending');
                  else if(sortOrder === 'parameters')
                    result = sortDataByParameter(result, 'ascending');
                else {
                    result = getSortedData(result, sortOrder);
                }
            
                setData(result);
            } catch (error) {
                console.error("Error fetching data:", error);
            }
            return;
        }
    
        // Handle search queries for specific patterns
        if (effectivePattern === "Quick Search") {
            const searchUrls = [
                `${baseUrl}/test?test=${encodedSearch}`,
                `${baseUrl}/id?id=${encodedSearch}`,
                `${baseUrl}/classification?classification=${encodedSearch}`,
                `${baseUrl}/group?group=${encodedSearch}`,
                `${baseUrl}/symbol?symbol=${encodedSearch}`,
                `${baseUrl}/parameters?parameters=${encodedSearch}`,
            ];
    
            try {
                const responses = await Promise.all(searchUrls.map(u => fetch(u, { testMethod: "GET", headers }).then(res => res.ok ? res.json() : Promise.reject(`Failed to fetch: ${u}`))));
                console.log("All parallel requests done:", responses);
                
                let mergedData = [];
                const seenIds = new Set();
                responses.forEach(result => {
                    result.forEach(item => {
                        if (!seenIds.has(item.id)) {
                            seenIds.add(item.id);
                            mergedData.push(item);
                        }
                    });
                });
                
                mergedData = getSortedData(mergedData, sortOrder);
    
                if (selectedGroup) {
                    mergedData = mergedData.filter(item => item.group?.trim().toLowerCase() === selectedGroup.trim().toLowerCase());
                }
                setData(mergedData);
            } catch (error) {
                console.error("Error fetching data:", error);
            }
            return;
        }
    
        // Default behavior for search
        if (effectiveSearchContent) {
            if (effectivePattern === "Test testMethod") {
                url = `${baseUrl}/testMethod?testMethod=${encodedSearch}`;
            } else if (effectivePattern === "Test parameters") {
                url = `${baseUrl}/parameters?parameters=${encodedSearch}`;
            } else if (effectivePattern === "Classification") {
                url = `${baseUrl}/classification?classification=${encodedSearch}`;
            } else if (effectivePattern === "Test group") {
                url = `${baseUrl}/group?group=${encodedSearch}`;
            } else if (effectivePattern === "Test name") {
                url = `${baseUrl}/test?test=${encodedSearch}`;
            }
        }
    
        if (sortOrder !== "default" && sortOrder !== "search") {
            url += (url.includes("?") ? "&" : "?") + `sort=${encodeURIComponent(sortOrder)}`;
        }
    
        console.log(`Final fetch from: ${url}`);
        try {
            const response = await fetch(url, { testMethod: "GET", headers });
            if (!response.ok) throw new Error(`Failed to fetch data. HTTP Status: ${response.status}`);
            let result = await response.json();
            result = getSortedData(result, sortOrder);
    
            if (selectedGroup) {
                result = result.filter(item => item.group?.trim().toLowerCase() === selectedGroup.trim().toLowerCase());
            }
            setData(result);
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };
    
    useEffect(() => {
        fetchData();
        // eslint-disable-next-line
    }, [viewMode, lastSearchContent, lastPattern, sortOrder, baseUrl, selectedGroup, token]);
    
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = data.slice(indexOfFirstItem, indexOfLastItem);
    const totalPages = Math.ceil(data.length / itemsPerPage);
    
    const nextPage = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
            window.scrollTo(0, 0);
        }
    };
    
    const prevPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
            window.scrollTo(0, 0);
        }
    };
    

    const handleJump = () => {
        const page = parseInt(jumpPage, 10);
        if (!isNaN(page) && page >= 1 && page <= totalPages) {
            setCurrentPage(page);
            window.scrollTo(0, 0);
        }
    };

    const handleSortChange = (order) => {
        setSortOrder(order);
        setCurrentPage(1); // Reset to the first page when sorting changes
        window.scrollTo(0, 0); // Scroll to the top of the page when sorting changes
    
        // Reset search sorting when switching away from 'search' order
        if (order !== 'search') {
            setSearchSort(false);
        }
    
        // If sorting by 'name', ensure that the selected group remains consistent
        if (order === "name") {
            setSelectedGroup(selectedGroup); // Retain the selected group when sorting by name
        }
    };
    
    // Handler for toggling the canvas nest animation
    const handleAnimationToggle = () => {
        if (animationEnabled) {
            // Disable the canvas nest animation
            window.canvasNestEnabled = false;
            setAnimationEnabled(false);
        } else {
            // Enable the canvas nest animation and restart it
            window.canvasNestEnabled = true;
            if (window.startCanvasNest) window.startCanvasNest();
            setAnimationEnabled(true);
        }
    };

    const handleBoxClick = (box, event) => {
        const viewportWidth = window.innerWidth;
        const viewportHeight = window.innerHeight;
        const boxWidth = 400;
        const boxHeight = 400;
        let x = event.clientX - 200;
        let y = event.clientY - 25;
        if (x + boxWidth > viewportWidth) {
            x = viewportWidth - boxWidth;
        }
        if (y + boxHeight > viewportHeight) {
            y = viewportHeight - boxHeight;
        }
        const alreadyOpen = selectedBoxes.some(selectedBox => selectedBox.id === box.id);
        if (alreadyOpen) {
            setSelectedBoxes(prev => prev.filter(selectedBox => selectedBox.id !== box.id));
        } else {
            setSelectedBoxes(prev => [...prev, { ...box, position: { x, y } }]);
        }
    };

    const closeFloatingDetails = (id) => {
        setSelectedBoxes(prev => prev.filter(box => box.id !== id));
    };

    const renderPaginationButtons = () => {
        if (totalPages <= 1) return null;
        const pages = [];
        pages.push(
            <button
                key={1}
                onClick={() => setCurrentPage(1)}
                className={currentPage === 1 ? 'active' : ''}
                disabled={currentPage === 1}
            >
                1
            </button>
        );
        if (currentPage > 3) {
            pages.push(<span key="dots1" className="dots">...</span>);
        }
        const start = Math.max(2, currentPage - 2);
        const end = Math.min(totalPages - 1, currentPage + 2);
        for (let i = start; i <= end; i++) {
            pages.push(
                <button
                    key={i}
                    onClick={() => setCurrentPage(i)}
                    className={currentPage === i ? 'active' : ''}
                    disabled={currentPage === i}
                >
                    {i}
                </button>
            );
        }
        if (currentPage < totalPages - 2) {
            pages.push(<span key="dots2" className="dots">...</span>);
        }
        pages.push(
            <button
                key={totalPages}
                onClick={() => setCurrentPage(totalPages)}
                className={currentPage === totalPages ? 'active' : ''}
                disabled={currentPage === totalPages}
            >
                {totalPages}
            </button>
        );
        return pages;
    };

    return (
        <div>
            {isSmallScreen && (
                <button
                    className={`sidebar-toggle ${isSidebarOpen ? 'open' : ''}`}
                    onClick={() => setIsSidebarOpen(prev => !prev)}
                >
                    ☰
                </button>
            )}
            <div className="container">
                <div className={`sidebar ${isSmallScreen ? (isSidebarOpen ? 'open' : 'closed') : ''}`}>
                <h3 className="page-header" style={{ marginTop: '60px' }}>Search Order</h3>
                    <div className="db-switch">
                        <button
                            className={`db-button ${databaseType === "soil" ? "active" : ""}`}
                            onClick={() => setDatabaseType("soil")}
                        >
                            Soil
                        </button>
                        <button
                            className={`db-button ${databaseType === "aggregate" ? "active" : ""} `}
                            onClick={() => setDatabaseType("aggregate")}
                        >
                            <p style={{ fontSize: '12px', margin: 0 }}>Aggregate</p>
                        </button>
                        <button
                            className={`db-button ${databaseType === "rocks" ? "active" : ""}`}
                            onClick={() => setDatabaseType("rocks")}
                        >
                            Rock
                        </button>
                        <button
                            className={`db-button ${databaseType === "concrete" ? "active" : ""}`}
                            onClick={() => setDatabaseType("concrete")}
                        >
                        <p style={{ fontSize: '12px', margin: 0 }}>Concrete</p>
                        </button>
                        <button
                            className={`db-button ${databaseType === "inSituTest" ? "active" : ""}`}
                            onClick={() => setDatabaseType("inSituTest")}
                        >
                        <p style={{ fontSize: '12px', margin: 0 }}>inSituTest</p>
                        </button>
                    </div>
                    {/* Toggle button to switch between full testlist and search results (only displayed if an initial search exists) */}
                    {lastSearchContent && (
                        <button
                            className="toggle-button"
                            onClick={() => setViewMode(!viewMode)}
                            style={{ marginTop: '10px', width: '100%' }}
                        >
                            {viewMode ? "Return to testlist" : "Return to search result"}
                        </button>
                    )}
                    {searchSort && (
                        <button
                            className={sortOrder === 'search' ? 'active' : ''}
                            onClick={() => handleSortChange('search')}
                        >
                            Search results order
                        </button>
                    )}
                    <button
                        className={sortOrder === 'test' ? 'active' : ''}
                        onClick={() => handleSortChange('test')}
                    >
                        Name Arrangement
                    </button>
                    <button
                        className={sortOrder === 'name' ? 'active' : ''}
                        onClick={() => handleSortChange('group')}
                    >
                        Group Arrangement
                    </button>
                    <button
                        className={sortOrder === 'classification' ? 'active' : ''}
                        onClick={() => handleSortChange('classification')}
                    >
                        AGS Code Arrangement
                    </button>
                    <button
                        className={sortOrder === 'testMethod' ? 'active' : ''}
                        onClick={() => handleSortChange('testMethod')}
                    >
                        Method Arrangement
                    </button>
                    <button
                        className={sortOrder === 'parameters' ? 'active' : ''}
                        onClick={() => handleSortChange('parameters')}
                    >
                        Parameter Arrangement
                    </button>

                    <h3>Filter by Group</h3>
                    <select
                        value={selectedGroup}
                        onChange={(e) => {
                            setSelectedGroup(e.target.value);
                            setCurrentPage(1);
                        }}
                    >
                        <option value="">All Groups</option>
                        {groups.map((group, index) => (
                            <option key={index} value={group}>{group}</option>
                        ))}
                    </select>
                    <h4>No of Tests per Page</h4>
                    <select
                        value={itemsPerPage}
                        onChange={(e) => {
                            setItemsPerPage(Number(e.target.value));
                            setCurrentPage(1);
                        }}
                    >
                        <option value="10">10</option>
                        <option value="20">20</option>
                        <option value="40">40</option>
                        <option value="50">50</option>
                        <option value="100">All Tests</option>
                    </select>
                </div>

                <div className="box-container">
                    {viewMode ? (
                        <div className="fixed-placeholder">
                            <h2>Search Results</h2>
                            <p className="search-text">
                                Displaying tests that best match <span className="search-content">"{lastSearchContent}"</span> with pattern <span className="search-pattern">"{lastPattern}"</span>.
                            </p>
                        </div>
                    ) : (

                        !isLoggedIn ? (
                            <div className="fixed-placeholder" style={{ position: 'relative', textAlign: 'center' }}>
                            <img src={lockIcon} alt="Locked" style={{ width: '120px', marginBottom: '20px' }} />
                            <h2>You are currently logged out. Please log in again to view tests.</h2>
                            </div>
                        ) : (
                        
                        <div className="fixed-placeholder" style={{ position: 'relative' }}>
                            <h2>Testlist</h2>
                            <p>
                                Information about all tests is presented here. You can use the sorting feature to quickly find tests of interest.
                            </p>
                            <p>Click on a test item to view the details.</p>
                            {isLoggedIn && (
                                <Link to="/additem">
                                    <button
                                        className="add-item-button"
                                        style={{ position: 'absolute', bottom: '10px', right: '10px' }}
                                    >
                                        Add Item
                                    </button>
                                </Link>
                            )}
                        </div>
                        )
                    )}
                    {currentItems.map((item) => (
                        <Box
                            key={item.id}
                            {...item}
                            onClick={(event) => handleBoxClick(item, event)}
                            isActive={selectedBoxes.some(selectedBox => selectedBox.id === item.id)}
                            searchcontent={viewMode ? lastSearchContent : ""}
                            pattern={viewMode ? lastPattern : ""}
                        />
                    ))}
                </div>

                <div
                    className="pagination"
                    style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '0.5rem',
                        flexWrap: 'wrap',
                        marginTop: '1rem'
                    }}
                >
                    <button onClick={prevPage} disabled={currentPage === 1}>
                        Previous
                    </button>
                    {renderPaginationButtons()}
                    <button onClick={nextPage} disabled={currentPage === totalPages}>
                        Next
                    </button>
                    <input
                        type="number"
                        placeholder="Page #"
                        value={jumpPage}
                        onChange={(e) => setJumpPage(e.target.value)}
                        min="1"
                        max={totalPages}
                        style={{ width: '60px' }}
                    />
                    <button onClick={handleJump}>Jump</button>
                </div>

                {selectedBoxes.map((box) => (
                    <FloatingDetails
                        key={box.id}
                        details={box}
                        onClose={closeFloatingDetails}
                        position={box.position}
                        searchcontent={viewMode ? lastSearchContent : ""}
                        pattern={viewMode ? lastPattern : ""}
                    />
                ))}
            </div>
        </div>
    );
};

export default PaginatedBoxes;
