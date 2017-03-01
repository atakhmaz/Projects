#include "shortestdistance.h"

ShortestDistance::ShortestDistance(char const* OutputFileName, char const* airportsFileName, char const* routesFileName, char const* airlinesFilename):filename(OutputFileName)
{
        ifstream in(OutputFileName);
        getline(in,line);
        if(line.compare("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
        {
            in.close();
            clock_t start= clock();
            out.open(OutputFileName);
            airports.open(airportsFileName);
            routes.open(routesFileName);
            airlines.open(airlinesFilename);
            parseAirports(dataMap);
            parseRoutes(dataMap);
            parseAirline(carrierNames);
            writeXML(dataMap, carrierNames);
            cout << "Parsing: " << (clock() - start)/(double)CLOCKS_PER_SEC << " seconds." << endl;
        }
        in.close();
        createTable();
}

void ShortestDistance::parseAirports(map<int, Vertex *> &dataMap)
{
    string airport_data[AIRPORT_LINE_SIZE];
    getline(airports, line);
    int airportID;
    while(getline(airports, line))
    {
        for(int j = 0; j < AIRPORT_LINE_SIZE; ++j)
        {
            getline(airports, line);
            airport_data[j] = extract(line);
        }
        airportID = stoi(airport_data[AIRPORT_ID]);
        dataMap[airportID] = new Vertex();
        dataMap[airportID]->airport_id = airportID;
        dataMap[airportID]->airport_iata = airport_data[IATA];
        dataMap[airportID]->airport_icao = airport_data[ICAO];
        dataMap[airportID]->airport_city = airport_data[AIRPORT_CITY];
        dataMap[airportID]->lat = stod(airport_data[LATITUDE]);
        dataMap[airportID]->longi = stod(airport_data[LONGITUDE]);
        dataMap[airportID]->airport_country = airport_data[COUNTRY];
        dataMap[airportID]->airport_name = airport_data[AIRPORT_NAME];
        while(line.compare("</airport>"))
            getline(airports, line);
    }
    airports.close();
}

void ShortestDistance::parseRoutes(map<int, Vertex *> &dataMap)
{
    string routes_data[ROUTES_LINE_SIZE];
    getline(routes, line);
    int airportID, destAirportID;
    long double sourceLat, sourceLong, destinationLat, destinationLong, deltaFI, deltaLambda, deltaSigma, distance;
    while(getline(routes, line))
    {
        for(int j = 0; j < ROUTES_LINE_SIZE; ++j)
        {
            getline(routes, line);
            routes_data[j] = extract(line);
        }
        if(routes_data[SOURCE_AIRPORT_ID].compare("0") &&
           routes_data[DESTINATION_AIRPORT_ID].compare("0") &&
           routes_data[DESTINATION_AIRPORT_ID].compare("\\N") &&
           routes_data[SOURCE_AIRPORT_ID].compare("\\N") &&
           routes_data[AIRLINE_ID].compare("\\N")
           )
        {
            airportID = stoi(routes_data[SOURCE_AIRPORT_ID]);
            sourceLat = (dataMap[airportID]->lat*PI)/180.0;
            sourceLong = (dataMap[airportID]->longi*PI)/180.0;
            destAirportID = stoi(routes_data[DESTINATION_AIRPORT_ID]);
            destinationLat = (dataMap[destAirportID]->lat*PI)/180.0;
            destinationLong = (dataMap[destAirportID]->longi*PI)/180.0;
            deltaFI = fabs(sourceLat - destinationLat);
            deltaLambda = fabs(sourceLong - destinationLong);
            deltaSigma = 2 * asin(sqrt(pow(sin(deltaFI/2),2) + cos(sourceLat) * cos(destinationLat) * pow(sin(deltaLambda/2),2)));
            distance = RADIUS_OF_EARTH * deltaSigma;
            dataMap[airportID]->routes.push_back(new Routes(stoi(routes_data[AIRLINE_ID]),
                                                            routes_data[DESTINATION_AIRPORT_CODE],
                                                            routes_data[AIRLINE_CODE],
                                                            dataMap[destAirportID]->airport_name,
                                                            dataMap[destAirportID]->airport_country,
                                                            dataMap[destAirportID]->airport_city,
                                                            destAirportID,
                                                            distance
                                                            ));
        }
        while(line.compare("</Routes>"))
            getline(routes, line);
    }
    routes.close();
}

void ShortestDistance::parseAirline(map<int,string> &carrierNames)
{
    string airline_data[AIRLINES_LINE_SIZE];
    getline(airlines, line);
    int j;
    while(getline(airlines, line))
    {
        j = 0;
        for(; j < 2; ++j)
        {
            getline(airlines, line);
            airline_data[j] = extract(line);
        }
        getline(airlines, line);
        for(; j < 5; ++j)
        {
            getline(airlines, line);
            airline_data[j] = extract(line);
        }
        getline(airlines, line);
        for(; j < 6; ++j)
        {
            getline(airlines, line);
            airline_data[j] = extract(line);
        }
        carrierNames[stoi(airline_data[AIRLINES_ID])] = airline_data[AIRLINE_NAME];
        while(line.compare("</Airlines>"))
            getline(airlines, line);
    }
    airlines.close();
}

void ShortestDistance::writeXML(map<int, Vertex *> &dataMap, map<int, string> &carrierNames)
{
    out << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" << endl;
    map<int,Vertex*>::const_iterator mt = dataMap.begin();
    for(; mt != dataMap.end(); ++mt)
    {
        out << "<Vertex>" << endl;
        out << "\t<Source Airport ID>" << mt->second->airport_id << "</Source Airport ID>" << endl;
        out << "\t<Source Airport Name>" << mt->second->airport_name << "</<Source Airport Name>" << endl;
        out << "\t<Source Airport IATA>" << mt->second->airport_iata << "</Source Airport IATA>" << endl;
        out << "\t<Source Airport City>" << mt->second->airport_city << "</Source Airport City>" << endl;
        out << "\t<Source Airport Country>" << mt->second->airport_country << "</Source Airport Country>" << endl;
        out << "\t<Source Airport Latitude>" << mt->second->lat << "</Source Airport Latitude>" << endl;
        out << "\t<Source Airport Longitude>" << mt->second->longi << "</Source Airport Longitude>" << endl;
        out << "\t<Edges>" << endl;
        sort(mt->second->routes.begin(),mt->second->routes.end(), sort_pred());
        vector<Routes*>::const_iterator it = mt->second->routes.begin();
        for(; it != mt->second->routes.end(); ++it)
        {
            out << "\t\t<Edge>" << endl;
            out << "\t\t\t<Destination Airport ID>" << (*it)->nextAirport << "</Destination Airport ID>" << endl;
            out << "\t\t\t<Destination Airport Name>" << (*it)->destination_airport_name << "</Destination Airport Name>" << endl;
            out << "\t\t\t<Destination Airport IATA>" << (*it)->destination_airport_iata << "</Destination Airport IATA>" << endl;
            out << "\t\t\t<Destination Airport City>" << (*it)->destination_airport_city << "</Destination Airport City>" << endl;
            out << "\t\t\t<Destination Airport Country>" << (*it)->destination_airport_country << "</Destination Airport Country>" << endl;
            out << "\t\t\t<Carrier>" << (*it)->airline_iata << "</Carrier>" << endl;
            out << "\t\t\t<Carrier Name>" << carrierNames[(*it)->airline_id] << "</Carrier Name>" << endl;
            out << "\t\t\t<Distance>" << (*it)->distance << "</Distance>" << endl;
            out << "\t\t\t<Carrier ID>" << (*it)->airline_id << "</Carrier ID>" << endl;
            out << "\t\t</Edge>" << endl;
        }
        out << "\t</Edges>" << endl;
        out << "</Vertex>" << endl;
    }
    out.close();
}

string ShortestDistance::extract(const string &line)
{
    int firstPos = line.find_first_of('>');
    int lastPos = line.find_first_of('<' , firstPos+1);
    return line.substr(firstPos+1, lastPos-firstPos-1);
}

void ShortestDistance::toUpper(string &aString)
{
    for (unsigned int i = 0; i < aString.size(); ++i)
        aString[i] = toupper(aString[i]);
}

void ShortestDistance::createTable()
{
    ifstream in(filename);
    getline(in,line);
    string airport_data[AIRPORT_DATA_SIZE];
    string edge_data[EDGE_DATA_SIZE];
    int airport_id;
    bool pushNew;
    while(getline(in,line))
    {
        for(int i = 0; i < AIRPORT_DATA_SIZE; ++i)
        {
            getline(in,line);
            airport_data[i] = extract(line);
        }
        airport_id = stoi(airport_data[0]);
        getline(in,line);
        table[airport_id] = new Vertex(airport_id, airport_data[1],airport_data[2],airport_data[3],airport_data[4],stod(airport_data[5]),stod(airport_data[6]));
        while(getline(in,line) && (line.compare("\t</Edges>") || !line.compare("\t\t<Edge>")))
        {
            for(int i = 0; i < EDGE_DATA_SIZE; ++i)
            {
                getline(in,line);
                edge_data[i] = extract(line);
            }
            getline(in,line);
            if(table[airport_id]->routes.empty())
                table[airport_id]->pushNewRoute(new Routes(edge_data[NEXT_AIRPORT_CARRIER_NAME], stoi(edge_data[NEXT_AIRPORT_ID]), stod(edge_data[NEXT_AIRPORT_DISTANCE])));
            else
            {
                pushNew = true;
                vector<Routes*>::const_iterator it = table[airport_id]->routes.begin();
                for(; it != table[airport_id]->routes.end(); ++it)
                {
                    if((*it)->nextAirport == stoi(edge_data[NEXT_AIRPORT_ID]))
                    {
                        (*it)->pushNewCarrier(edge_data[NEXT_AIRPORT_CARRIER_NAME]);
                        pushNew = false;
                        break;
                    }
                }
                if(pushNew)
                    table[airport_id]->pushNewRoute(new Routes(edge_data[NEXT_AIRPORT_CARRIER_NAME], stoi(edge_data[NEXT_AIRPORT_ID]), stod(edge_data[NEXT_AIRPORT_DISTANCE])));
            }
        }
        getline(in, line);
    }
}

vector<Vertex*> ShortestDistance::shortestDistance(string &airport1, string &airport2)
{
    toUpper(airport1);
    toUpper(airport2);
    vector<Vertex*> vec;
    Vertex* u;
    set<Vertex*, Prioritize> Q;
    int starting_airport = -1, destination_airport = -1;
    for(map<int,Vertex*>::const_iterator mt = table.begin(); mt != table.end(); ++mt)
        if(!mt->second->airport_iata.empty())
        {
            if(!mt->second->airport_iata.compare(airport1))
                starting_airport = mt->second->airport_id;
            else if(!mt->second->airport_iata.compare(airport2))
                destination_airport = mt->second->airport_id;
        }
    if(starting_airport == -1)
    {
        cout << "Starting airport not found." << endl;
        return vec;
    }
    else if(destination_airport == -1)
    {
        cout << "Destination airport not found." << endl;
        return vec;
    }
    table[starting_airport]->cost = 0;
    Q.insert(table[starting_airport]);
    while(!Q.empty())
    {
        u = *Q.begin();
        if(u->airport_id == destination_airport)
        {
            while(table[u->airport_id]->prev)
            {
                vec.insert(vec.begin(), table[u->airport_id]->prev);
                u = table[u->airport_id]->prev;
            }
            vec.push_back(table[destination_airport]);
            return vec;
        }
        Q.erase(Q.begin());
        for(vector<Routes*>::iterator neighbor = u->routes.begin(); neighbor != u->routes.end(); ++neighbor)
        {
            double alt = u->cost + (*neighbor)->distance;
            if(alt < table[(*neighbor)->nextAirport]->cost)
            {
                table[(*neighbor)->nextAirport]->cost = alt;
                table[(*neighbor)->nextAirport]->prev = u;
                if(Q.find(table[(*neighbor)->nextAirport]) == Q.end())
                    Q.insert(table[(*neighbor)->nextAirport]);
            }
        }
    }
    return vec;
}
