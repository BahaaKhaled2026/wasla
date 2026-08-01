import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class StatusScreen extends StatefulWidget {
  const StatusScreen({
    super.key,
    required this.title,
    required this.healthyLabel,
    required this.unhealthyLabel,
    required this.requestIdLabel,
    required this.checkedAtLabel,
    required this.buildLabel,
    this.actions = const [],
  });

  final String title;
  final String healthyLabel;
  final String unhealthyLabel;
  final String requestIdLabel;
  final String checkedAtLabel;
  final String buildLabel;
  final List<Widget> actions;

  @override
  State<StatusScreen> createState() => _StatusScreenState();
}

class _StatusScreenState extends State<StatusScreen> {
  Map<String, dynamic>? _status;
  bool _loading = true;
  bool _error = false;

  @override
  void initState() {
    super.initState();
    _fetchStatus();
  }

  Future<void> _fetchStatus() async {
    setState(() {
      _loading = true;
      _error = false;
    });
    try {
      final response = await http
          .get(Uri.parse('http://localhost:8080/api/v1/status'))
          .timeout(const Duration(seconds: 5));
      if (response.statusCode == 200) {
        setState(() {
          _status = jsonDecode(response.body) as Map<String, dynamic>;
          _loading = false;
        });
      } else {
        setState(() {
          _error = true;
          _loading = false;
        });
      }
    } catch (_) {
      setState(() {
        _error = true;
        _loading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final healthy = _status?['health']?['status'] == 'UP';

    return Scaffold(
      appBar: AppBar(title: Text(widget.title), actions: widget.actions),
      body: RefreshIndicator(
        onRefresh: _fetchStatus,
        child: ListView(
          padding: const EdgeInsets.all(24),
          children: [
            if (_loading) const Center(child: CircularProgressIndicator()),
            if (!_loading && _error) _buildCard(context, healthy: false, message: widget.unhealthyLabel),
            if (!_loading && !_error && _status != null) ...[
              _buildCard(
                context,
                healthy: healthy,
                message: healthy ? widget.healthyLabel : widget.unhealthyLabel,
              ),
              const SizedBox(height: 16),
              _buildDetailRow(widget.requestIdLabel, _status!['requestId']?.toString() ?? ''),
              _buildDetailRow(widget.checkedAtLabel, _status!['serverTime']?.toString() ?? ''),
              _buildDetailRow(widget.buildLabel, _status!['buildRevision']?.toString() ?? ''),
              const SizedBox(height: 16),
              ..._buildComponentList(_status!['health']?['components']),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildCard(BuildContext context, {required bool healthy, required String message}) {
    final scheme = Theme.of(context).colorScheme;
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: healthy ? scheme.primaryContainer : scheme.errorContainer,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Text(message, style: Theme.of(context).textTheme.titleMedium),
    );
  }

  Widget _buildDetailRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(width: 100, child: Text(label, style: const TextStyle(fontWeight: FontWeight.w500))),
          Expanded(child: Text(value, style: const TextStyle(fontFamily: 'monospace'))),
        ],
      ),
    );
  }

  List<Widget> _buildComponentList(dynamic components) {
    if (components is! Map) return [];
    return components.entries.map<Widget>((e) => Text('${e.key}: ${e.value}')).toList();
  }
}