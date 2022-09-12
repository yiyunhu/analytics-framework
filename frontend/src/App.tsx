import React from 'react';
import './App.css';
import { Component } from 'react';
import * as echarts from 'echarts';
import {Container, Button, Form} from 'react-bootstrap';

interface Props{
}

interface Input{
  value:any;
  plugins:Array<any>;
}

class App extends Component<Props,Input> {

  constructor(props:Props) {
    super(props);
    this.state = {
      value: "",
      plugins:[],
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event: { target: { value: any; }; }) {
    this.setState({value: event.target.value});
  }

  handleSubmit(event: { preventDefault: () => void; }) {
    // alert('A name was submitted: ' + this.state.value);
    const obj:any = this.display(this.state.value);
    event.preventDefault();
  }

  visualize(){
    const plugins:Array<any> = this.state.plugins;
    var nodes:Array<any> = Array.from(document.querySelectorAll("div.display"));
    for(var i = 0; i<nodes.length;i++){
      var node:HTMLElement = nodes[i];
      var chart = echarts.init(node);
      chart.setOption(plugins[i]);
    }
    
  }

  test(p:any){
    alert("length " + p["displayList"].length);
  }

  async display(name:String){
    const href = "search?text="+name;
    const response = await fetch(href);
    const json = await response.json();
    this.setState({plugins:json["displayList"],});
    this.visualize();
  }

  render() {
    const plugins = this.state.plugins.map((option:any) =>
      <div className="display" style={{height:350}}></div>
    );
    return (
      <Container className='p-3'>
        <Container>
          <Container className="p-5 mb-4 bg-light rounded-3">
            <h1 className='header'>
              Welcome to ChooseU Application
            </h1>
          </Container>
          <Form onSubmit={this.handleSubmit}>
            <Form.Group className="mb-3" controlId="formText">
              <Form.Label>Enter the name of the university</Form.Label>
              <Form.Control type="input" placeholder="E.g. Carnegie Mellon University" value={this.state.value} onChange={this.handleChange}/>
              <Form.Text className="text-muted">
                Click the search button to see the recent reputation trend of the university.
              </Form.Text>
            </Form.Group>
            <Button variant="primary" className="mr-1" type='submit'>
              Search
            </Button>
          </Form>
        </Container>
        <Container>
        <div className='visualization'>
          {plugins}
        </div>
        </Container>
      </Container>
    );
  }

}

export default App;
